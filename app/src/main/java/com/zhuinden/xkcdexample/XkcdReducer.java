package com.zhuinden.xkcdexample;

import android.annotation.SuppressLint;

import com.zhuinden.statebundle.StateBundle;
import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.Reducer;
import com.zhuinden.xkcdexample.redux.ReduxStore;
import com.zhuinden.xkcdexample.redux.State;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.zhuinden.xkcdexample.XkcdActions.COMIC_CHANGED;
import static com.zhuinden.xkcdexample.XkcdActions.COMIC_SAVED;
import static com.zhuinden.xkcdexample.XkcdActions.DOWNLOAD_CURRENT;
import static com.zhuinden.xkcdexample.XkcdActions.DOWNLOAD_DEFAULT;
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
import static com.zhuinden.xkcdexample.XkcdState.putMax;
import static com.zhuinden.xkcdexample.XkcdState.putNumber;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class XkcdReducer
        implements Reducer {
    private Random random;
    private ReduxStore reduxStore;
    private XkcdService xkcdService;
    private XkcdMapper xkcdMapper;

    public XkcdReducer(XkcdService xkcdService, XkcdMapper xkcdMapper, Random random) {
        this.xkcdService = xkcdService;
        this.xkcdMapper = xkcdMapper;
        this.random = random;
    }

    @SuppressLint("NewApi")
    @Override
    public Single<State> reduce(State state, Action action) {
        StateBundle stateBundle = new StateBundle(state.state());
        int current = current(stateBundle);
        final int max = max(stateBundle);
        boolean isDownloading = isDownloading(stateBundle);
        int number;
        switch(action.type()) {
            case START_DOWNLOAD:
                putDownloading(stateBundle, true);
                return Single.just(State.create(stateBundle, action));
            case FINISH_DOWNLOAD:
                putDownloading(stateBundle, false);
                return Single.just(State.create(stateBundle, action));
            case NEXT_COMIC:
                if(!isDownloading && current < max) {
                    putCurrent(stateBundle, current + 1);
                }
                return Single.just(State.create(stateBundle, action));
            case PREVIOUS_COMIC:
                if(!isDownloading && current > 1) {
                    putCurrent(stateBundle, current - 1);
                }
                return Single.just(State.create(stateBundle, action));
            case RANDOM_COMIC:
                if(!isDownloading && max > 0) {
                    putCurrent(stateBundle, random.nextInt(max) + 1);
                }
                return Single.just(State.create(stateBundle, action));
            case GO_TO_LATEST:
                return downloadDefault().flatMap(result -> {
                    int _number = number(result);
                    putCurrent(stateBundle, _number);
                    if(max == 0 || _number > max) {
                        putMax(stateBundle, number(result));
                    }
                    return Single.just(State.create(stateBundle, action));
                });
            case RETRY_DOWNLOAD:
                if(current == 0) {
                    return downloadDefault().flatMap(result -> {
                        putCurrent(stateBundle, number(result));
                        if(max == 0) {
                            putMax(stateBundle, number(result));
                        }
                        return Single.just(State.create(stateBundle, action));
                    });
                } else {
                    return downloadNumber(current).flatMap((ignored) -> Single.just(State.create(stateBundle, action)));
                }
            case JUMP_TO_NUMBER:
                number = number(action.payload());
                if(number > 0 && number <= max) {
                    putCurrent(stateBundle, number);
                    return downloadNumber(number).flatMap((ignored) -> Single.just(State.create(stateBundle, action)));
                }
                return Single.just(State.create(stateBundle, action));
            case NETWORK_ERROR:
                try(Realm realm = Realm.getDefaultInstance()) {
                    Number maxNum = realm.where(XkcdComic.class).max(XkcdComicFields.NUM);
                    if(maxNum != null && max <= 0) {
                        putMax(stateBundle, maxNum.intValue());
                        putCurrent(stateBundle, maxNum.intValue());
                    }
                }
                return Single.just(State.create(stateBundle, action));
            case DOWNLOAD_CURRENT:
                return downloadNumber(current).flatMap(o -> Single.just(State.create(stateBundle, action)));
            case DOWNLOAD_DEFAULT:
                return downloadDefault().flatMap(o -> Single.just(State.create(stateBundle, action)));
            case COMIC_SAVED:
                putCurrent(stateBundle, number(action.payload()));
                return Single.just(State.create(stateBundle, action));
            case INITIALIZE:
                if(current == 0) {
                    return downloadDefault().flatMap(result -> {
                        int _number = number(result);
                        if(_number != 0) {
                            putCurrent(stateBundle, _number);
                            putMax(stateBundle, _number);
                        } else {
                            int maxNum = initMax(action.payload());
                            if(maxNum != -1) {
                                putCurrent(stateBundle, maxNum);
                                putMax(stateBundle, maxNum);
                            }
                        }
                        return Single.just(State.create(stateBundle, action));
                    });
                } else {
                    return Single.just(State.create(stateBundle, action));
                }
            case OPEN_JUMP_DIALOG:
            case OPEN_LINK:
            case SHOW_ALT_TEXT:
            case OPEN_IN_BROWSER:
            case COMIC_CHANGED:
                return Single.just(State.create(stateBundle, action));
        }
        return Single.just(state);
    }

    @SuppressWarnings("Convert2MethodRef")
    private Single<StateBundle> downloadDefault() {
        return download(service -> service.getDefault());
    }

    private Single<StateBundle> downloadNumber(int number) {
        return download(service -> service.getNumber(number));
    }

    private interface MethodSelector {
        Single<XkcdResponse> selectMethod(XkcdService xkcdService)
                throws IOException;
    }

    @SuppressWarnings("NewApi")
    private Single<StateBundle> download(final MethodSelector methodSelector) {
        return Single.fromCallable(() -> {
            StateBundle stateBundle = new StateBundle();
            try {
                //reduxStore.dispatch(Action.create(XkcdActions.START_DOWNLOAD)); // FIXME RACE CONDITION IF CLICKS ARE TOO FAST!
                XkcdResponse xkcdResponse = methodSelector.selectMethod(xkcdService).blockingGet();
                XkcdComic xkcdComic = xkcdMapper.from(xkcdResponse);
                try(Realm r = Realm.getDefaultInstance()) {
                    r.executeTransaction(realm -> {
                        realm.insertOrUpdate(xkcdComic);
                        putNumber(stateBundle, xkcdComic.getNum());
                        reduxStore.dispatch(Action.create(COMIC_SAVED, stateBundle));
                    });
                }
            } catch(Exception e) {
                reduxStore.dispatch(Action.create(NETWORK_ERROR));
            } finally {
                //reduxStore.dispatch(Action.create(XkcdActions.FINISH_DOWNLOAD)); // FIXME RACE CONDITION IF CLICKS ARE TOO FAST!
            }
            return stateBundle;
        }).subscribeOn(Schedulers.io());
    }

    public void setStore(ReduxStore store) {
        this.reduxStore = store;
    }
}
