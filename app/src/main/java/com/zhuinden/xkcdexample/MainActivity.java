package com.zhuinden.xkcdexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Response;

public class MainActivity
        extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.xkcd_image)
    ImageView image;

    @OnClick(R.id.xkcd_previous)
    public void previous() {
        if(!isDownloading && current > 1) {
            current--;
            openOrDownloadCurrent();
        }
    }

    @OnClick(R.id.xkcd_next)
    public void next() {
        if(!isDownloading && current < max) {
            current++;
            openOrDownloadCurrent();
        }
    }

    @OnClick(R.id.xkcd_random)
    public void random() {
        if(!isDownloading) {
            current = random.nextInt(max) + 1;
            openOrDownloadCurrent();
        }
    }

    private void openOrDownloadCurrent() {
        if(!queryAndShowComicIfExists()) {
            downloadCurrent();
        }
    }

    private void downloadCurrent() {
        executor.execute(new DownloadTask((service -> service.getNumber(current).execute())));
    }

    @OnLongClick(R.id.xkcd_image)
    public boolean longClickImage() {
        if(xkcdComic != null) {
            Toast.makeText(this, xkcdComic.getAlt(), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @OnClick(R.id.xkcd_image)
    public void clickImage() {
        if(xkcdComic != null && xkcdComic.getLink() != null && !"".equals(xkcdComic.getLink())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(xkcdComic.getLink()));
            startActivity(intent);
        }
    }

    volatile boolean isDownloading = false;

    XkcdMapper xkcdMapper;
    XkcdService xkcdService;
    Executor executor;
    Random random;

    volatile int current = 0;

    volatile int max = 0;

    Realm realm;

    RealmResults<XkcdComic> results;
    XkcdComic xkcdComic;

    RealmChangeListener<RealmResults<XkcdComic>> realmChangeListener = element -> {
        if(!realm.isClosed()) {
            queryAndShowComicIfExists();
        }
    };

    AlertDialog jumpDialog;

    private void openJumpDialog() {
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

    private void openOrDownloadByNumber(int number) {
        if(number > 0 && number <= max) {
            current = number;
            openOrDownloadCurrent();
        }
    }

    private boolean queryAndShowComicIfExists() {
        XkcdComic xkcdComic = getCurrentXkcdComic();
        if(xkcdComic != null) {
            storeAndOpenComic(xkcdComic);
            return true;
        }
        return false;
    }

    private void storeAndOpenComic(XkcdComic xkcdComic) {
        this.xkcdComic = xkcdComic;
        updateUi(xkcdComic);
    }

    private XkcdComic getCurrentXkcdComic() {
        return realm.where(XkcdComic.class).equalTo(XkcdComicFields.NUM, current).findFirst();
    }

    private void updateUi(XkcdComic xkcdComic) {
        getSupportActionBar().setTitle("#" + xkcdComic.getNum() + ": " + xkcdComic.getTitle());
        Glide.with(this).load(xkcdComic.getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);
    }

    @Override
    @SuppressWarnings("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            current = savedInstanceState.getInt("current");
            max = savedInstanceState.getInt("max");
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        results = realm.where(XkcdComic.class).findAll();
        results.addChangeListener(realmChangeListener);

        xkcdService = CustomApplication.get(this).xkcdService();
        xkcdMapper = CustomApplication.get(this).xkcdMapper();
        executor = CustomApplication.get(this).executor();
        random = CustomApplication.get(this).random();

        queryAndShowComicIfExists();
        if(current == 0) {
            downloadDefault();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current", current);
        outState.putInt("max", max);
    }

    @Override
    protected void onDestroy() {
        results.removeChangeListener(realmChangeListener);
        realm.close();
        realm = null;
        cancelJumpDialogIfShowing();
        super.onDestroy();
    }

    private void cancelJumpDialogIfShowing() {
        if(jumpDialog != null && jumpDialog.isShowing()) {
            jumpDialog.cancel();
            jumpDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_jump:
                openJumpDialog();
                return true;
            case R.id.action_retry:
                if(current == 0) {
                    downloadDefault();
                } else {
                    downloadCurrent();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadDefault() {
        executor.execute(new DownloadTask((service -> service.getDefault().execute())));
    }

    private interface MethodSelector {
        Response<XkcdResponse> selectMethod(XkcdService xkcdService)
                throws IOException;
    }

    private class DownloadTask
            implements Runnable {
        MethodSelector methodSelector;

        public DownloadTask(MethodSelector methodSelector) {
            this.methodSelector = methodSelector;
        }

        @Override
        @SuppressWarnings("NewApi")
        public void run() {
            try {
                isDownloading = true;
                Response<XkcdResponse> _xkcdResponse = methodSelector.selectMethod(xkcdService);
                XkcdResponse xkcdResponse = _xkcdResponse.body();
                XkcdComic xkcdComic = xkcdMapper.from(xkcdResponse);
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction(realm -> {
                        realm.insertOrUpdate(xkcdComic);
                        if(current == 0) {
                            max = xkcdComic.getNum();
                        }
                        current = xkcdComic.getNum();
                    });
                }
            } catch(IOException e) {
                Log.e(TAG, "Could not download XKCD data", e);
            } finally {
                isDownloading = false;
            }
        }
    }
}