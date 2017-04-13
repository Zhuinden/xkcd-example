/*
 * Copyright 2017 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.xkcdexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.ReduxStore;
import com.zhuinden.xkcdexample.util.Unit;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity
        extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.xkcd_image)
    ImageView image;

    @OnClick(R.id.xkcd_previous)
    public void previous() {
        reduxStore.dispatch(Action.create(XkcdActions.PREVIOUS_COMIC));
        if(!isDownloading && current > 1) {
            modifyCurrentAndUpdateComic(current - 1);
        }
    }

    @OnClick(R.id.xkcd_next)
    public void next() {
        reduxStore.dispatch(Action.create(XkcdActions.NEXT_COMIC));
        if(!isDownloading && current < max) {
            modifyCurrentAndUpdateComic(current + 1);
        }
    }

    @OnClick(R.id.xkcd_random)
    public void random() {
        reduxStore.dispatch(Action.create(XkcdActions.RANDOM_COMIC));
        if(!isDownloading) {
            modifyCurrentAndUpdateComic(random.nextInt(max) + 1);
        }
    }

    private void openOrDownloadByNumber(int number) {
        reduxStore.dispatch(Action.create(XkcdActions.JUMP_TO_NUMBER));
        if(number > 0 && number <= max) {
            modifyCurrentAndUpdateComic(number);
        }
    }

    private void modifyCurrentAndUpdateComic(int number) {
        this.current = number;
        openOrDownloadCurrent();
    }

    private void openOrDownloadCurrent() {
        if(!queryAndShowComicIfExists()) {
            downloadCurrent();
        }
    }

    private void downloadCurrent() {
        download(service -> service.getNumber(current));
    }

    @SuppressWarnings("NewApi")
    private void download(final MethodSelector methodSelector) {
        Single.fromCallable((Callable<Object>) () -> {
            try {
                reduxStore.dispatch(Action.create(XkcdActions.START_DOWNLOAD));
                isDownloading = true;
                XkcdResponse xkcdResponse = methodSelector.selectMethod(xkcdService).blockingGet();
                XkcdComic xkcdComic = xkcdMapper.from(xkcdResponse);
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction(realm -> {
                        realm.insertOrUpdate(xkcdComic);
                        if(current == 0 || xkcdComic.getNum() > max) {
                            max = xkcdComic.getNum();
                        }
                        current = xkcdComic.getNum();
                    });
                }
            } catch(IOException e) {
                reduxStore.dispatch(Action.create(XkcdActions.NETWORK_ERROR));
                runOnUiThread(MainActivity.this::handleNetworkError);
            } finally {
                reduxStore.dispatch(Action.create(XkcdActions.FINISH_DOWNLOAD));
                isDownloading = false;
            }
            return Unit.INSTANCE;
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private boolean queryAndShowComicIfExists() {
        XkcdComic xkcdComic = getCurrentXkcdComic();
        if(xkcdComic != null) {
            storeAndOpenComic(xkcdComic);
            return true;
        }
        return false;
    }

    private XkcdComic getCurrentXkcdComic() {
        return realm.where(XkcdComic.class).equalTo(XkcdComicFields.NUM, current).findFirst();
    }

    private void storeAndOpenComic(XkcdComic xkcdComic) {
        this.xkcdComic = xkcdComic;
        updateUi(xkcdComic);
    }

    private void updateUi(XkcdComic xkcdComic) {
        getSupportActionBar().setTitle("#" + xkcdComic.getNum() + ": " + xkcdComic.getTitle());
        Glide.with(this).load(xkcdComic.getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
    }

    @OnLongClick(R.id.xkcd_image)
    public boolean longClickImage() {
        if(xkcdComic != null) {
            reduxStore.dispatch(Action.create(XkcdActions.SHOW_ALT_TEXT));
            showAltText(xkcdComic);
            return true;
        }
        return false;
    }

    private void showAltText(XkcdComic xkcdComic) {
        Toast.makeText(this, xkcdComic.getAlt(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.xkcd_image)
    public void clickImage() {
        if(xkcdComic != null) {
            reduxStore.dispatch(Action.create(XkcdActions.OPEN_LINK));
            openLinkIfExists(xkcdComic);
        }
    }

    private void openLinkIfExists(XkcdComic xkcdComic) {
        reduxStore.dispatch(Action.create(XkcdActions.OPEN_LINK));
        if(xkcdComic.getLink() != null && !"".equals(xkcdComic.getLink())) {
            openUriWithBrowser(Uri.parse(xkcdComic.getLink()));
        }
    }

    volatile boolean isDownloading = false;

    XkcdMapper xkcdMapper;

    XkcdService xkcdService;

    ReduxStore reduxStore;

    Random random;

    volatile int current = 0;

    volatile int max = 0;

    Realm realm;

    RealmResults<XkcdComic> results;

    XkcdComic xkcdComic;

    RealmChangeListener<RealmResults<XkcdComic>> realmChangeListener = element -> {
        reduxStore.dispatch(Action.create(XkcdActions.COMIC_CHANGED));
        if(!realm.isClosed()) {
            queryAndShowComicIfExists();
        }
    };

    AlertDialog jumpDialog;

    private void openJumpDialog() {
        reduxStore.dispatch(Action.create(XkcdActions.OPEN_JUMP_DIALOG));
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_jump, null, false);
        final EditText jumpNumbers = ButterKnife.findById(dialogView, R.id.jump_numbers);
        jumpDialog = new AlertDialog.Builder(this) //
                .setTitle(R.string.jump_to) //
                .setView(dialogView) //
                .setPositiveButton(R.string.jump, (dialog, which) -> {
                    startJumpToNumber(jumpNumbers);
                    hideKeyboard(jumpNumbers);
                }).setNegativeButton(R.string.cancel, (dialog, which) -> {
                    hideKeyboard(jumpNumbers);
                }).setOnCancelListener(dialog -> {
                    hideKeyboard(jumpNumbers);
                }).create();
        jumpNumbers.setOnEditorActionListener((v, actionId, event) -> {
            if((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                startJumpToNumber(jumpNumbers);
                hideKeyboard(jumpNumbers);
                cancelJumpDialogIfShowing();
                return true;
            }
            return false;
        });
        jumpDialog.show();
        showKeyboard(jumpNumbers);
    }

    private void startJumpToNumber(EditText jumpNumbers) {
        String _number = jumpNumbers.getText().toString();
        if(!"".equals(_number)) {
            int number = Integer.parseInt(_number);
            openOrDownloadByNumber(number);
        }
    }

    private void cancelJumpDialogIfShowing() {
        if(jumpDialog != null && jumpDialog.isShowing()) {
            jumpDialog.cancel();
            jumpDialog = null;
        }
    }

    private void showKeyboard(View view) {
        view.postDelayed(() -> {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
        }, 300);
    }

    private void hideKeyboard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(),
                0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // always is needed here
    }

    Disposable disposable;

    @Override
    @SuppressWarnings("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        reduxStore = CustomApplication.get(this).reduxStore();
        if(savedInstanceState != null) {
            current = savedInstanceState.getInt("current");
            max = savedInstanceState.getInt("max");
            if(reduxStore.isAtInitialState()) {
                reduxStore.setInitialState(savedInstanceState.getParcelable("state"));
            }
        }
        super.onCreate(savedInstanceState);
        MainScopeListener mainScopeListener = (MainScopeListener) getSupportFragmentManager().findFragmentByTag(
                "SCOPE_LISTENER");
        if(mainScopeListener == null) {
            mainScopeListener = new MainScopeListener();
            getSupportFragmentManager().beginTransaction().add(mainScopeListener, "SCOPE_LISTENER").commitNow();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        results = realm.where(XkcdComic.class).findAll();
        results.addChangeListener(realmChangeListener);

        this.disposable = reduxStore.state().subscribe(stateChange -> {
            Log.i(TAG, "[" + stateChange + "]");
        });

        xkcdService = CustomApplication.get(this).xkcdService();
        xkcdMapper = CustomApplication.get(this).xkcdMapper();
        random = CustomApplication.get(this).random();

        reduxStore.dispatch(Action.create(XkcdActions.INITIALIZE));
        queryAndShowComicIfExists();
        if(current == 0) {
            downloadDefault();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("state", reduxStore.getState());
        outState.putInt("current", current);
        outState.putInt("max", max);
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        disposable = null;
        results.removeChangeListener(realmChangeListener);
        realm.close();
        realm = null;
        cancelJumpDialogIfShowing();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_latest:
                reduxStore.dispatch(Action.create(XkcdActions.GO_TO_LATEST));
                downloadDefault();
                return true;
            case R.id.action_jump:
                reduxStore.dispatch(Action.create(XkcdActions.OPEN_JUMP_DIALOG));
                openJumpDialog();
                return true;
            case R.id.action_retry:
                reduxStore.dispatch(Action.create(XkcdActions.RETRY_DOWNLOAD));
                if(current == 0) {
                    downloadDefault();
                } else {
                    downloadCurrent();
                }
                return true;
            case R.id.action_open_browser:
                reduxStore.dispatch(Action.create(XkcdActions.OPEN_IN_BROWSER));
                openUriWithBrowser(Uri.parse("https://xkcd.com/" + current));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openUriWithBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @SuppressWarnings("Convert2MethodRef")
    private void downloadDefault() {
        download(service -> service.getDefault());
    }

    public static ReduxStore getStore(Context context) {
        // noinspection ResourceType
        return (ReduxStore) context.getSystemService("REDUX_STORE");
    }

    private interface MethodSelector {
        Single<XkcdResponse> selectMethod(XkcdService xkcdService)
                throws IOException;
    }

    private void handleNetworkError() {
        reduxStore.dispatch(Action.create(XkcdActions.NETWORK_ERROR));
        if(current != 0) { // not first start-up
            showNetworkError();
            return;
        }
        Number maxNum = realm.where(XkcdComic.class).max(XkcdComicFields.NUM);
        if(maxNum == null) { // no image downloaded yet at all
            showNetworkError();
            return;
        }
        // handle if no internet but can reload from cache
        max = maxNum.intValue();
        modifyCurrentAndUpdateComic(max);
    }

    private void showNetworkError() {
        Toast.makeText(MainActivity.this, R.string.please_retry_with_active_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if("REDUX_STORE".equals(name)) {
            return reduxStore;
        }
        return super.getSystemService(name);
    }
}