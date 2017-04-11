package com.zhuinden.xkcdexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
        XkcdComic xkcdComic = getCurrentXkcdComic();
        if(xkcdComic == null) {
            downloadCurrent();
        } else {
            this.xkcdComic = xkcdComic;
            updateUi(xkcdComic);
        }
    }

    @SuppressWarnings("NewApi")
    private void downloadCurrent() {
        executor.execute(() -> {
            try {
                isDownloading = true;
                Response<XkcdResponse> _xkcdResponse = xkcdService.getNumber(current).execute();
                handleXkcdResponse(_xkcdResponse);
            } catch(IOException e) {
                Log.e(TAG, "Could not download XKCD data", e);
            } finally {
                isDownloading = false;
            }
        });
    }

    @SuppressWarnings("NewApi")
    private void handleXkcdResponse(Response<XkcdResponse> _xkcdResponse) {
        XkcdResponse xkcdResponse = _xkcdResponse.body();
        XkcdComic xkcdComic = xkcdMapper.from(xkcdResponse);
        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction(realm -> realm.insertOrUpdate(xkcdComic));
        }
        if(current == 0) {
            max = xkcdComic.getNum();
        }
        current = xkcdComic.getNum();
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
            xkcdComic = getCurrentXkcdComic();
            if(xkcdComic != null) {
                updateUi(xkcdComic);
            }
        }
    };

    private XkcdComic getCurrentXkcdComic() {
        return realm.where(XkcdComic.class).equalTo(XkcdComicFields.NUM, current).findFirst();
    }

    private void updateUi(XkcdComic xkcdComic) {
        getSupportActionBar().setTitle("#" + xkcdComic.getNum() + ": " + xkcdComic.getTitle());
        Glide.with(this).load(xkcdComic.getImg()).into(image);
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

        xkcdComic = getCurrentXkcdComic();
        if(xkcdComic != null) {
            updateUi(xkcdComic);
        }
        if(current == 0) {
            executor.execute(() -> {
                try {
                    isDownloading = true;
                    Response<XkcdResponse> xkcdResponse = xkcdService.getDefault().execute();
                    handleXkcdResponse(xkcdResponse);
                } catch(IOException e) {
                    Log.e(TAG, "Could not download XKCD data", e);
                } finally {
                    isDownloading = false;
                }
            });
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
        super.onDestroy();
    }
}
