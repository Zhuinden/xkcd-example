package com.zhuinden.xkcdexample;

import android.annotation.SuppressLint;

import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.Reducer;
import com.zhuinden.xkcdexample.redux.State;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.zhuinden.xkcdexample.XkcdActions.COMIC_CHANGED;
import static com.zhuinden.xkcdexample.XkcdActions.COMIC_SAVED;
import static com.zhuinden.xkcdexample.XkcdActions.FINISH_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.GO_TO_LATEST;
import static com.zhuinden.xkcdexample.XkcdActions.INITIALIZE;
import static com.zhuinden.xkcdexample.XkcdActions.JUMP_TO_NUMBER;
import static com.zhuinden.xkcdexample.XkcdActions.NETWORK_ERROR;
import static com.zhuinden.xkcdexample.XkcdActions.NEXT_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_IN_BROWSER;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_JUMP_DIALOG;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_LINK;
import static com.zhuinden.xkcdexample.XkcdActions.PREVIOUS_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RANDOM_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RETRY_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.SHOW_ALT_TEXT;
import static com.zhuinden.xkcdexample.XkcdActions.START_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdState.current;
import static com.zhuinden.xkcdexample.XkcdState.initMax;
import static com.zhuinden.xkcdexample.XkcdState.isDownloading;
import static com.zhuinden.xkcdexample.XkcdState.max;
import static com.zhuinden.xkcdexample.XkcdState.number;
import static com.zhuinden.xkcdexample.XkcdState.putCurrent;
import static com.zhuinden.xkcdexample.XkcdState.putDownloading;
import static com.zhuinden.xkcdexample.XkcdState.putInitMax;
import static com.zhuinden.xkcdexample.XkcdState.putMax;
import static com.zhuinden.xkcdexample.XkcdState.putNumber;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class XkcdReducer
        implements Reducer {
    private Random random;
    private XkcdService xkcdService;
    private XkcdMapper xkcdMapper;

    public XkcdReducer(XkcdService xkcdService, XkcdMapper xkcdMapper, Random random) {
        this.xkcdService = xkcdService;
        this.xkcdMapper = xkcdMapper;
        this.random = random;
    }

    @SuppressLint("NewApi")
    @Override
    public Observable<State> reduce(State state, Action action) {
        switch(action.type()) {
            case START_DOWNLOAD:
                return startDownload(state, action);
            case FINISH_DOWNLOAD:
                return finishDownload(state, action);
            case NEXT_COMIC:
                return nextComic(state, action);
            case PREVIOUS_COMIC:
                return previousComic(state, action);
            case RANDOM_COMIC:
                return randomComic(state, action);
            case GO_TO_LATEST:
                return goToLatest(state, action);
            case RETRY_DOWNLOAD:
                return retryDownload(state, action);
            case JUMP_TO_NUMBER:
                return jumpToNumber(state, action);
            case NETWORK_ERROR:
                return networkError(state, action);
            case COMIC_SAVED:
                return comicSaved(state, action);
            case INITIALIZE:
                return initialize(state, action);
            case OPEN_JUMP_DIALOG:
            case OPEN_LINK:
            case SHOW_ALT_TEXT:
            case OPEN_IN_BROWSER:
            case COMIC_CHANGED:
                return createState(action, state.state());
        }
        return Observable.just(state);
    }

    private Observable<State> initialize(State state, Action action) {
        int current = current(state.state());
        if(current == 0) {
            return downloadDefault(state, action).flatMap(result -> {
                CopyOnWriteStateBundle stateBundle = result.state();
                int number = number(result.state());
                if(number != 0) {
                    stateBundle = putCurrent(stateBundle, number);
                    stateBundle = putMax(stateBundle, number);
                } else {
                    int maxNum = initMax(action.payload());
                    if(maxNum != -1) {
                        stateBundle = putCurrent(stateBundle, maxNum);
                        stateBundle = putMax(stateBundle, maxNum);
                    }
                }
                return createState(action, stateBundle);
            });
        } else {
            return createState(action, state.state());
        }
    }

    private Observable<State> comicSaved(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        stateBundle = putCurrent(stateBundle, number(action.payload()));
        return createState(action, stateBundle);
    }

    private Observable<State> downloadCurrent(State state, Action action) {
        return downloadNumber(state, action, current(state.state())).flatMap(result -> createState(result.action(), result.state()));
    }

    private Observable<State> networkError(State state, Action action) {
        int maxNum = initMax(action.payload());
        int max = max(state.state());
        CopyOnWriteStateBundle stateBundle = state.state();
        if(maxNum != -1 && max <= 0) {
            stateBundle = putMax(stateBundle, maxNum);
            stateBundle = putCurrent(stateBundle, maxNum);
        }
        return createState(action, stateBundle);
    }

    private Observable<State> jumpToNumber(State state, Action action) {
        int number = number(action.payload());
        int max = max(state.state());
        CopyOnWriteStateBundle stateBundle = state.state();
        if(number > 0 && number <= max) {
            stateBundle = putCurrent(stateBundle, number);
            return downloadNumber(State.create(stateBundle, action), action, number);
        } else {
            return createState(action, stateBundle);
        }
    }

    private Observable<State> retryDownload(State state, Action action) {
        int current = current(state.state());
        if(current == 0) {
            return goToLatest(state, action);
        } else {
            return downloadCurrent(state, action);
        }
    }

    private Observable<State> goToLatest(State state, Action action) {
        return downloadDefault(state, action).flatMap(result -> handleDownloadedLatest(result));
    }

    private ObservableSource<? extends State> handleDownloadedLatest(State state) {
        CopyOnWriteStateBundle stateBundle = state.state();
        int number = number(state.action().payload());
        int max = max(state.state());
        if(number != 0) {
            if(max == 0 || number > max) {
                stateBundle = putMax(stateBundle, number);
            }
            stateBundle = putCurrent(stateBundle, number);
        }
        return createState(state.action(), stateBundle);
    }

    private Observable<State> finishDownload(State state, Action action) {
        return changeDownloadState(state, action, false);
    }

    private Observable<State> startDownload(State state, Action action) {
        return changeDownloadState(state, action, true);
    }

    private Observable<State> randomComic(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        boolean isDownloading = isDownloading(stateBundle);
        final int max = max(state.state());
        if(!isDownloading && max > 0) {
            stateBundle = putCurrent(stateBundle, random.nextInt(max) + 1);
        }
        return createState(action, stateBundle);
    }

    private Observable<State> previousComic(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        int current = current(stateBundle);
        boolean isDownloading = isDownloading(stateBundle);
        if(!isDownloading && current > 1) {
            stateBundle = putCurrent(stateBundle, current - 1);
        }
        return createState(action, stateBundle);
    }

    private Observable<State> nextComic(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        int current = current(stateBundle);
        boolean isDownloading = isDownloading(stateBundle);
        final int max = max(state.state());
        if(!isDownloading && current < max) {
            stateBundle = putCurrent(stateBundle, current + 1);
        }
        return createState(action, stateBundle);
    }

    private Observable<State> changeDownloadState(State state, Action action, boolean isDownloading) {
        CopyOnWriteStateBundle bundle = putDownloading(state.state(), isDownloading);
        return createState(action, bundle);
    }

    private Observable<State> createState(Action action, CopyOnWriteStateBundle stateBundle) {
        return Observable.just(State.create(stateBundle, action));
    }

    @SuppressWarnings("Convert2MethodRef")
    private Observable<State> downloadDefault(State state, Action action) {
        return download(state, action, service -> service.getDefault());
    }

    private Observable<State> downloadNumber(State state, Action action, int number) {
        return download(state, action, service -> service.getNumber(number));
    }

    private interface MethodSelector {
        Single<XkcdResponse> selectMethod(XkcdService xkcdService)
                throws IOException;
    }

    @SuppressWarnings("NewApi")
    private Observable<State> download(final State initialState, Action action, final MethodSelector methodSelector) {
        return Observable.create((ObservableOnSubscribe<State>) emitter -> {
            reduce(initialState, Action.create(XkcdActions.START_DOWNLOAD, action.payload())).flatMap((state) -> {
                emitter.onNext(state);
                try {
                    XkcdResponse xkcdResponse = methodSelector.selectMethod(xkcdService).blockingGet();
                    XkcdComic xkcdComic = xkcdMapper.from(xkcdResponse);
                    CopyOnWriteStateBundle stateBundle = writeComicToRealm(state.state(), xkcdComic);
                    return reduce(state, Action.create(COMIC_SAVED, stateBundle));
                } catch(Exception e) {
                    return reduce(state, Action.create(NETWORK_ERROR, readNetworkErrorParamFromRealm(action.payload())));
                }
            }).flatMap((state) -> {
                emitter.onNext(state);
                return reduce(state, Action.create(XkcdActions.FINISH_DOWNLOAD));
            }).subscribe(state -> {
                emitter.onNext(state);
                emitter.onNext(State.create(state.state(), action));
                emitter.onComplete();
            });
        }).subscribeOn(Schedulers.io());
    }

    @SuppressWarnings("NewApi")
    private CopyOnWriteStateBundle readNetworkErrorParamFromRealm(CopyOnWriteStateBundle param) {
        try(Realm realm = Realm.getDefaultInstance()) {
            Number maxNum = realm.where(XkcdComic.class).max(XkcdComicFields.NUM);
            param = putInitMax(param, maxNum == null ? -1 : maxNum.intValue());
        }
        return param;
    }

    @SuppressWarnings("NewApi")
    private CopyOnWriteStateBundle writeComicToRealm(CopyOnWriteStateBundle stateBundle, XkcdComic xkcdComic) {
        try(Realm realm = Realm.getDefaultInstance()) {
            try {
                realm.beginTransaction();
                realm.insertOrUpdate(xkcdComic);
                stateBundle = putNumber(stateBundle, xkcdComic.getNum());
                realm.commitTransaction();
            } catch(Exception e) {
                if(realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
            }
        }
        return stateBundle;
    }
}
