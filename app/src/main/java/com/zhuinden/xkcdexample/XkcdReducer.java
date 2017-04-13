package com.zhuinden.xkcdexample;

import io.reactivex.Single;

import static com.zhuinden.xkcdexample.Actions.COMIC_CHANGED;
import static com.zhuinden.xkcdexample.Actions.FINISH_DOWNLOAD;
import static com.zhuinden.xkcdexample.Actions.GO_TO_LATEST;
import static com.zhuinden.xkcdexample.Actions.INITIALIZE;
import static com.zhuinden.xkcdexample.Actions.JUMP_TO_NUMBER;
import static com.zhuinden.xkcdexample.Actions.NEXT_COMIC;
import static com.zhuinden.xkcdexample.Actions.OPEN_IN_BROWSER;
import static com.zhuinden.xkcdexample.Actions.OPEN_LINK;
import static com.zhuinden.xkcdexample.Actions.PREVIOUS_COMIC;
import static com.zhuinden.xkcdexample.Actions.RANDOM_COMIC;
import static com.zhuinden.xkcdexample.Actions.RETRY_DOWNLOAD;
import static com.zhuinden.xkcdexample.Actions.SHOW_ALT_TEXT;
import static com.zhuinden.xkcdexample.Actions.START_DOWNLOAD;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class XkcdReducer
        implements Reducer {
    @Override
    public Single<State> reduce(State state, Action action) {
        switch(action.type()) {
            case INITIALIZE:
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
