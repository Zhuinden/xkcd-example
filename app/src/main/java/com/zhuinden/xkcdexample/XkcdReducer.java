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
        CopyOnWriteStateBundle stateBundle = state.state();
        int current = current(state.state());
        final int max = max(state.state());
        boolean isDownloading = isDownloading(state.state());
        int number;
        switch(action.type()) {
            case START_DOWNLOAD:
                stateBundle = putDownloading(stateBundle, true);
                return createState(action, stateBundle);
            case FINISH_DOWNLOAD:
                stateBundle = putDownloading(stateBundle, false);
                return createState(action, stateBundle);
            case NEXT_COMIC:
                if(!isDownloading && current < max) {
                    stateBundle = putCurrent(stateBundle, current + 1);
                }
                return createState(action, stateBundle);
            case PREVIOUS_COMIC:
                if(!isDownloading && current > 1) {
                    stateBundle = putCurrent(stateBundle, current - 1);
                }
                return createState(action, stateBundle);
            case RANDOM_COMIC:
                if(!isDownloading && max > 0) {
                    stateBundle = putCurrent(stateBundle, random.nextInt(max) + 1);
                }
                return createState(action, stateBundle);
            case GO_TO_LATEST:
                return downloadDefault(State.create(stateBundle, action)).flatMap(result -> {
                    CopyOnWriteStateBundle _stateBundle = result.state();
                    int _number = number(result.state());
                    if(_number != 0) {
                        _stateBundle = putCurrent(_stateBundle, _number);
                        int _max = max(result.state());
                        if(_max == 0 || _number > _max) {
                            _stateBundle = putMax(_stateBundle, number(result.state()));
                        }
                    }
                    return createState(result.action(), _stateBundle);
                });
            case RETRY_DOWNLOAD:
                if(current == 0) {
                    return downloadDefault(State.create(stateBundle, action)).flatMap(result -> {
                        CopyOnWriteStateBundle _stateBundle = result.state();
                        int _number = number(result.state());
                        if(_number != 0) {
                            _stateBundle = putCurrent(_stateBundle, _number);
                            int _max = max(result.state());
                            if(_max == 0) {
                                _stateBundle = putMax(_stateBundle, _number);
                            }
                        }
                        return createState(result.action(), _stateBundle);
                    });
                } else {
                    return downloadNumber(State.create(stateBundle, action), current);
                }
            case JUMP_TO_NUMBER:
                number = number(action.payload());
                if(number > 0 && number <= max) {
                    stateBundle = putCurrent(stateBundle, number);
                    return downloadNumber(State.create(stateBundle, action), number);
                }
                return createState(action, stateBundle);
            case NETWORK_ERROR:
                int maxNum = initMax(state.action().payload());
                if(maxNum != -1 && max <= 0) {
                    stateBundle = putMax(stateBundle, maxNum);
                    stateBundle = putCurrent(stateBundle, maxNum);
                }
                return createState(action, stateBundle);
            case DOWNLOAD_CURRENT:
                return downloadNumber(State.create(stateBundle, action), current).flatMap(result -> {
                    CopyOnWriteStateBundle _stateBundle = result.state();
                    return createState(result.action(), _stateBundle);
                });
            case DOWNLOAD_DEFAULT:
                return downloadDefault(State.create(stateBundle, action)).flatMap(result -> {
                    CopyOnWriteStateBundle _stateBundle = result.state();
                    return createState(result.action(), _stateBundle);
                });
            case COMIC_SAVED:
                stateBundle = putCurrent(stateBundle, number(action.payload()));
                return createState(action, stateBundle);
            case INITIALIZE:
                if(current == 0) {
                    return downloadDefault(State.create(stateBundle, action)).flatMap(result -> {
                        CopyOnWriteStateBundle _stateBundle = result.state();
                        int _number = number(result.state());
                        if(_number != 0) {
                            _stateBundle = putCurrent(_stateBundle, _number);
                            _stateBundle = putMax(_stateBundle, _number);
                        } else {
                            int _maxNum = initMax(action.payload());
                            if(_maxNum != -1) {
                                _stateBundle = putCurrent(_stateBundle, _maxNum);
                                _stateBundle = putMax(_stateBundle, _maxNum);
                            }
                        }
                        return createState(result.action(), _stateBundle);
                    });
                } else {
                    return createState(action, stateBundle);
                }
            case OPEN_JUMP_DIALOG:
            case OPEN_LINK:
            case SHOW_ALT_TEXT:
            case OPEN_IN_BROWSER:
            case COMIC_CHANGED:
                return createState(action, stateBundle);
        }
        return Observable.just(state);
    }

    private Observable<State> createState(Action action, CopyOnWriteStateBundle stateBundle) {
        return Observable.just(State.create(stateBundle, action));
    }

    @SuppressWarnings("Convert2MethodRef")
    private Observable<State> downloadDefault(State state) {
        return download(state, service -> service.getDefault());
    }

    private Observable<State> downloadNumber(State state, int number) {
        return download(state, service -> service.getNumber(number));
    }

    private interface MethodSelector {
        Single<XkcdResponse> selectMethod(XkcdService xkcdService)
                throws IOException;
    }

    @SuppressWarnings("NewApi")
    private Observable<State> download(final State initialState, final MethodSelector methodSelector) {
        return Observable.create((ObservableOnSubscribe<State>) emitter -> {
            CopyOnWriteStateBundle stateBundle = initialState.state();
            State state = initialState;
            try {
                state = reduce(initialState,
                        Action.create(XkcdActions.START_DOWNLOAD)).blockingFirst(); // TODO: somehow turn these into flatMap()?
                emitter.onNext(state);

                XkcdResponse xkcdResponse = methodSelector.selectMethod(xkcdService).blockingGet();
                XkcdComic xkcdComic = xkcdMapper.from(xkcdResponse);
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
                state = reduce(state, Action.create(COMIC_SAVED, stateBundle)).blockingFirst();
                emitter.onNext(state);
            } catch(Exception e) {
                CopyOnWriteStateBundle param = new CopyOnWriteStateBundle();
                try(Realm realm = Realm.getDefaultInstance()) {
                    Number maxNum = realm.where(XkcdComic.class).max(XkcdComicFields.NUM);
                    param = putInitMax(param, maxNum == null ? -1 : maxNum.intValue());
                }
                state = reduce(state, Action.create(NETWORK_ERROR, param)).blockingFirst();
                emitter.onNext(state);
            } finally {
                state = reduce(state, Action.create(XkcdActions.FINISH_DOWNLOAD)).blockingFirst();
                emitter.onNext(state);
            }
            emitter.onNext(State.create(state.state(), state.action()));
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
