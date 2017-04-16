package com.zhuinden.xkcdexample;

import com.zhuinden.statebundle.StateBundle;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executor;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Owner on 2017. 04. 16..
 */

public class XkcdPresenter {
    public interface ViewContract {
        void updateUi(XkcdComic xkcdComic);

        void showAltText(XkcdComic xkcdComic);

        void openLink(String uri);

        void openJumpDialog();

        void doOnUiThread(Runnable runnable);

        void showNetworkError();
    }

    ViewContract viewContract;

    public void attachView(ViewContract viewContract) {
        this.viewContract = viewContract;
        onAttach();
        initializeView();
    }

    public void detachView() {
        onDetach();
        this.viewContract = null;
    }

    public void restoreState(StateBundle presenterState) {
        current = presenterState.getInt("current");
        max = presenterState.getInt("max");
    }

    public StateBundle saveState() {
        StateBundle stateBundle = new StateBundle();
        stateBundle.putInt("current", current);
        stateBundle.putInt("max", max);
        return stateBundle;
    }

    ////////

    private void onAttach() {
        results = realm.where(XkcdComic.class).findAll();
        results.addChangeListener(realmChangeListener);
    }

    private void onDetach() {
        results.removeChangeListener(realmChangeListener);
    }

    private void initializeView() {
        queryAndShowComicIfExists();
        if(current == 0) {
            downloadDefault();
        }
    }

    Realm realm;

    RealmResults<XkcdComic> results;

    XkcdComic xkcdComic;

    XkcdMapper xkcdMapper;

    XkcdService xkcdService;

    Executor executor;

    Random random;

    public XkcdPresenter(Realm realm, XkcdMapper xkcdMapper, XkcdService xkcdService, Executor executor, Random random) {
        this.realm = realm;
        this.xkcdMapper = xkcdMapper;
        this.xkcdService = xkcdService;
        this.executor = executor;
        this.random = random;
    }

    RealmChangeListener<RealmResults<XkcdComic>> realmChangeListener = element -> {
        if(!realm.isClosed()) {
            queryAndShowComicIfExists();
        }
    };

    volatile boolean isDownloading = false;

    volatile int current = 0;

    volatile int max = 0;

    public void openPreviousComic() {
        if(!isDownloading && current > 1) {
            modifyCurrentAndUpdateComic(current - 1);
        }
    }

    public void openNextComic() {
        if(!isDownloading && current < max) {
            modifyCurrentAndUpdateComic(current + 1);
        }
    }

    public void openRandomComic() {
        if(!isDownloading) {
            modifyCurrentAndUpdateComic(random.nextInt(max) + 1);
        }
    }

    private void openOrDownloadByNumber(int number) {
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

    private void download(MethodSelector methodSelector) {
        executor.execute(new DownloadTask(methodSelector));
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
        if(viewContract != null) {
            viewContract.updateUi(xkcdComic);
        }
    }

    public boolean showAltText() {
        if(xkcdComic != null) {
            if(viewContract != null) {
                viewContract.showAltText(xkcdComic);
            }
            return true;
        }
        return false;
    }

    public void openLink() {
        if(xkcdComic != null && xkcdComic.getLink() != null && !"".equals(xkcdComic.getLink())) {
            if(viewContract != null) {
                viewContract.openLink(xkcdComic.getLink());
            }
        }
    }


    public void startJumpToNumber(String numberText) {
        if(!"".equals(numberText)) {
            int number = Integer.parseInt(numberText);
            openOrDownloadByNumber(number);
        }
    }

    public void goToLatest() {
        downloadDefault();
    }

    public void jumpToNumber() {
        if(viewContract != null) {
            viewContract.openJumpDialog();
        }
    }

    public void retryDownload() {
        if(current == 0) {
            downloadDefault();
        } else {
            downloadCurrent();
        }
    }

    public void openInBrowser() {
        if(viewContract != null) {
            viewContract.openLink("https://xkcd.com/" + current);
        }
    }

    @SuppressWarnings("Convert2MethodRef")
    private void downloadDefault() {
        download(service -> service.getDefault());
    }

    private interface MethodSelector {
        Call<XkcdResponse> selectMethod(XkcdService xkcdService)
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
                Response<XkcdResponse> _xkcdResponse = methodSelector.selectMethod(xkcdService).execute();
                XkcdResponse xkcdResponse = _xkcdResponse.body();
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
                if(viewContract != null) {
                    viewContract.doOnUiThread(() -> handleNetworkError());
                }
            } finally {
                isDownloading = false;
            }
        }
    }

    @SuppressWarnings("NewApi")
    private void handleNetworkError() {
        if(current != 0) { // not first start-up
            if(viewContract != null) {
                viewContract.showNetworkError();
            }
            return;
        }
        Number maxNum = realm.where(XkcdComic.class).max(XkcdComicFields.NUM);
        if(maxNum == null) { // no image downloaded yet at all
            if(viewContract != null) {
                viewContract.showNetworkError();
            }
            return;
        }
        // handle if no internet but can reload from cache
        max = maxNum.intValue();
        modifyCurrentAndUpdateComic(max);
    }
}
