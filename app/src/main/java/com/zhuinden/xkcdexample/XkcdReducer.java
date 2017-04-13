package com.zhuinden.xkcdexample;

import com.zhuinden.statebundle.StateBundle;
import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.Reducer;
import com.zhuinden.xkcdexample.redux.State;

import io.reactivex.Single;

import static com.zhuinden.xkcdexample.XkcdActions.COMIC_CHANGED;
import static com.zhuinden.xkcdexample.XkcdActions.FINISH_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.GO_TO_LATEST;
import static com.zhuinden.xkcdexample.XkcdActions.INITIALIZE;
import static com.zhuinden.xkcdexample.XkcdActions.JUMP_TO_NUMBER;
import static com.zhuinden.xkcdexample.XkcdActions.NEXT_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_IN_BROWSER;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_LINK;
import static com.zhuinden.xkcdexample.XkcdActions.PREVIOUS_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RANDOM_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RETRY_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.SHOW_ALT_TEXT;
import static com.zhuinden.xkcdexample.XkcdActions.START_DOWNLOAD;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class XkcdReducer
        implements Reducer {
    @Override
    public Single<State> reduce(State state, Action action) {
        StateBundle stateBundle;
        switch(action.type()) {
            case INITIALIZE:
                stateBundle = new StateBundle(state.state());
                break;
            case START_DOWNLOAD:
                break;
            case FINISH_DOWNLOAD:
                break;
            case COMIC_CHANGED:
                break;
            case NEXT_COMIC:
                break;
            case PREVIOUS_COMIC:
                break;
            case RANDOM_COMIC:
                break;
            case GO_TO_LATEST:
                break;
            case RETRY_DOWNLOAD:
                break;
            case OPEN_IN_BROWSER:
                break;
            case JUMP_TO_NUMBER:
                break;
            case OPEN_LINK:
                break;
            case SHOW_ALT_TEXT:
                break;
        }
        return Single.just(state);
    }
}
