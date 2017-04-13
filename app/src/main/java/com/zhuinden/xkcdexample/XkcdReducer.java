package com.zhuinden.xkcdexample;

import com.zhuinden.statebundle.StateBundle;
import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.Reducer;
import com.zhuinden.xkcdexample.redux.State;

import io.reactivex.Single;

import static com.zhuinden.xkcdexample.XkcdActions.COMIC_CHANGED;
import static com.zhuinden.xkcdexample.XkcdActions.FINISH_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.GO_TO_LATEST;
import static com.zhuinden.xkcdexample.XkcdActions.JUMP_TO_NUMBER;
import static com.zhuinden.xkcdexample.XkcdActions.NETWORK_ERROR;
import static com.zhuinden.xkcdexample.XkcdActions.NEXT_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_IN_BROWSER;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_LINK;
import static com.zhuinden.xkcdexample.XkcdActions.PREVIOUS_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RANDOM_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RETRY_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.SHOW_ALT_TEXT;
import static com.zhuinden.xkcdexample.XkcdActions.START_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdState.putDownloading;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class XkcdReducer
        implements Reducer {
    @Override
    public Single<State> reduce(State state, Action action) {
        StateBundle stateBundle;
        switch(action.type()) {
            case START_DOWNLOAD:
                stateBundle = new StateBundle(state.state());
                putDownloading(stateBundle, true);
                return Single.just(State.create(stateBundle, action));
            case FINISH_DOWNLOAD:
                stateBundle = new StateBundle(state.state());
                putDownloading(stateBundle, false);
                return Single.just(State.create(stateBundle, action));
            case COMIC_CHANGED:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out.
                return Single.just(State.create(stateBundle, action));
            case NEXT_COMIC:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out: download with retrofit
                return Single.just(State.create(stateBundle, action));
            case PREVIOUS_COMIC:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out: download with retrofit
                return Single.just(State.create(stateBundle, action));
            case RANDOM_COMIC:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out: download with retrofit
                return Single.just(State.create(stateBundle, action));
            case GO_TO_LATEST:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out: download with retrofit
                return Single.just(State.create(stateBundle, action));
            case RETRY_DOWNLOAD:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out: download with retrofit
                return Single.just(State.create(stateBundle, action));
            case OPEN_IN_BROWSER:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out
                return Single.just(State.create(stateBundle, action));
            case JUMP_TO_NUMBER:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out
                return Single.just(State.create(stateBundle, action));
            case OPEN_LINK:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out
                return Single.just(State.create(stateBundle, action));
            case SHOW_ALT_TEXT:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out
                return Single.just(State.create(stateBundle, action));
            case NETWORK_ERROR:
                stateBundle = new StateBundle(state.state());
                // TODO: figure this out
                return Single.just(State.create(stateBundle, action));
        }
        return Single.just(state);
    }
}
