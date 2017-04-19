package com.zhuinden.xkcdexample;

import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.Middleware;
import com.zhuinden.xkcdexample.redux.ReduxStore;
import com.zhuinden.xkcdexample.redux.State;

import java.io.IOException;

import io.reactivex.Single;
import io.realm.Realm;

import static com.zhuinden.xkcdexample.XkcdState.current;
import static com.zhuinden.xkcdexample.XkcdState.number;

/**
 * Created by Zhuinden on 2017.04.19..
 */

public class XkcdMiddlewareDownload
        extends Middleware {
    private XkcdService xkcdService;
    private XkcdMapper xkcdMapper;

    public XkcdMiddlewareDownload(XkcdService xkcdService, XkcdMapper xkcdMapper) {
        this.xkcdMapper = xkcdMapper;
        this.xkcdService = xkcdService;
    }

    @Override
    public Interception doBefore() {
        return (reduxStore, action) -> {
            // TODO: make this work
            return Single.just(action);
        };
    }

    @Override
    public Interception doAfter() {
        return (reduxStore, action) -> {
            // TODO: make this work
            int current = current(reduxStore.getState().state());
            switch(action.type()) {
                case XkcdActions.JUMP_TO_NUMBER:
                    return downloadNumber(reduxStore, action, number(action.payload())) //
                            .flatMap(state -> Single.just(action));
                case XkcdActions.HANDLE_DOWNLOAD_RETRY:
                    if(current == 0) {
                        reduxStore.dispatch(Action.create(XkcdActions.HANDLE_GO_TO_LATEST, action.payload()));
                    } else {
                        reduxStore.dispatch(Action.create(XkcdActions.HANDLE_DOWNLOAD_CURRENT));
                    }
                    break;
                case XkcdActions.INITIALIZE:
                    if(current == 0) {
                        reduxStore.dispatch(Action.create(XkcdActions.HANDLE_INITIALIZE_NO_DEFAULT));
                    }
                    break;
            }
            return Single.just(action);
        };
    }

    @SuppressWarnings("Convert2MethodRef")
    private Single<State> downloadDefault(ReduxStore reduxStore, Action action) {
        return download(reduxStore, action, service -> service.getDefault());
    }

    private Single<State> downloadNumber(ReduxStore reduxStore, Action action, int number) {
        return download(reduxStore, action, service -> service.getNumber(number));
    }

    private interface MethodSelector {
        Single<XkcdResponse> selectMethod(XkcdService xkcdService)
                throws IOException;
    }

    @SuppressWarnings("NewApi")
    private Single<State> download(ReduxStore store, Action action, final MethodSelector methodSelector) {
        return Single.just(store.getState()); // TODO: make this work
//        State initialState = store.getState();
//        return Single.create((SingleOnSubscribe<Pair<State, Action>>) emitter -> {
//            emitter.onNext(State.create(initialState.state(), action), action);
//            reduce(initialState, Action.create(XkcdActions.START_DOWNLOAD, action.payload())).concatMap((state) -> {
//                emitter.onNext(state);
//                try {
//                    XkcdResponse xkcdResponse = methodSelector.selectMethod(xkcdService).blockingGet();
//                    XkcdEntity xkcdComic = xkcdMapper.from(xkcdResponse);
//                    writeComicToRealm(xkcdComic); // side-effect
//                    return reduce(state,
//                            Action.create(COMIC_SAVED, putNumber(state.action().payload(), xkcdComic.getNum())));
//                } catch(Exception e) {
//                    int initMax = readNetworkErrorParamFromRealm(); // side-effect
//                    return reduce(state, Action.create(NETWORK_ERROR, putInitMax(state.action().payload(), initMax)));
//                }
//            }).concatMap((state) -> {
//                emitter.onNext(state);
//                return reduce(state, Action.create(XkcdActions.FINISH_DOWNLOAD));
//            }).subscribe(state -> {
//                emitter.onNext(state);
//                emitter.onComplete();
//            });
//        }, BackpressureStrategy.MISSING).subscribeOn(Schedulers.io());
    }

    @SuppressWarnings("NewApi")
    private int readNetworkErrorParamFromRealm() {
        try(Realm realm = Realm.getDefaultInstance()) {
            Number maxNum = realm.where(XkcdEntity.class).max(XkcdComicFields.NUM);
            return maxNum == null ? -1 : maxNum.intValue();
        }
    }

    @SuppressWarnings("NewApi")
    private void writeComicToRealm(XkcdEntity xkcdComic) {
        try(Realm realm = Realm.getDefaultInstance()) {
            try {
                realm.beginTransaction();
                realm.insertOrUpdate(xkcdComic);
                realm.commitTransaction();
            } catch(Exception e) {
                if(realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw new RuntimeException(e);
            }
        }
    }
}
