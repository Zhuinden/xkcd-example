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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.Realm;

public class XkcdActivity
        extends AppCompatActivity
        implements XkcdPresenter.ViewContract {
    private static final String TAG = "XkcdActivity";

    XkcdPresenter xkcdPresenter;

    @BindView(R.id.xkcd_image)
    ImageView image;

    @OnClick(R.id.xkcd_previous)
    public void previous() {
        xkcdPresenter.openPreviousComic();
    }

    @OnClick(R.id.xkcd_next)
    public void next() {
        xkcdPresenter.openNextComic();
    }

    @OnClick(R.id.xkcd_random)
    public void random() {
        xkcdPresenter.openRandomComic();
    }

    @Override
    public void updateUi(XkcdComic xkcdComic) {
        getSupportActionBar().setTitle("#" + xkcdComic.getNum() + ": " + xkcdComic.getTitle());
        Glide.with(this).load(xkcdComic.getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
    }

    @OnLongClick(R.id.xkcd_image)
    public boolean longClickImage() {
        return xkcdPresenter.showAltText();
    }

    @Override
    public void showAltText(XkcdComic xkcdComic) {
        Toast.makeText(this, xkcdComic.getAlt(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.xkcd_image)
    public void clickImage() {
        xkcdPresenter.openLink();
    }

    @Override
    public void openLink(String uri) {
        openUriWithBrowser(Uri.parse(uri));
    }

    Realm realm;

    AlertDialog jumpDialog;

    @Override
    public void openJumpDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_jump, null, false);
        final EditText jumpNumbers = ButterKnife.findById(dialogView, R.id.jump_numbers);
        jumpDialog = new AlertDialog.Builder(this) //
                .setTitle(R.string.jump_to) //
                .setView(dialogView) //
                .setPositiveButton(R.string.jump, (dialog, which) -> {
                    xkcdPresenter.startJumpToNumber(jumpNumbers.getText().toString());
                    hideKeyboard(jumpNumbers);
                }).setNegativeButton(R.string.cancel, (dialog, which) -> {
                    hideKeyboard(jumpNumbers);
                }).setOnCancelListener(dialog -> {
                    hideKeyboard(jumpNumbers);
                }).create();
        jumpNumbers.setOnEditorActionListener((v, actionId, event) -> {
            if((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                xkcdPresenter.startJumpToNumber(jumpNumbers.getText().toString());
                hideKeyboard(jumpNumbers);
                cancelJumpDialogIfShowing();
                return true;
            }
            return false;
        });
        jumpDialog.show();
        showKeyboard(jumpNumbers);
    }

    @Override
    public void doOnUiThread(Runnable runnable) {
        runOnUiThread(runnable);
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
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // always is needed here
    }

    @Override
    @SuppressWarnings("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        xkcdPresenter = new XkcdPresenter(realm,
                CustomApplication.get(this).xkcdMapper(),
                CustomApplication.get(this).xkcdService(),
                CustomApplication.get(this).executor(),
                CustomApplication.get(this).random());
        if(savedInstanceState != null) {
            xkcdPresenter.restoreState(savedInstanceState.getParcelable("PRESENTER_STATE"));
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        xkcdPresenter.attachView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("PRESENTER_STATE", xkcdPresenter.saveState());
    }

    @Override
    protected void onDestroy() {
        xkcdPresenter.detachView();
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
                xkcdPresenter.goToLatest();
                return true;
            case R.id.action_jump:
                xkcdPresenter.jumpToNumber();
                return true;
            case R.id.action_retry:
                xkcdPresenter.retryDownload();
                return true;
            case R.id.action_open_browser:
                xkcdPresenter.openInBrowser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openUriWithBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(XkcdActivity.this, R.string.please_retry_with_active_internet, Toast.LENGTH_SHORT).show();
    }
}