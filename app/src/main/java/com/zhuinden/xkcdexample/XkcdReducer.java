package com.zhuinden.xkcdexample;

import android.annotation.SuppressLint;

import com.zhuinden.xkcdexample.redux.Action;
import com.zhuinden.xkcdexample.redux.Reducer;
import com.zhuinden.xkcdexample.redux.State;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

import static com.zhuinden.xkcdexample.XkcdActions.COMIC_CHANGED;
import static com.zhuinden.xkcdexample.XkcdActions.COMIC_SAVED;
import static com.zhuinden.xkcdexample.XkcdActions.FINISH_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.GO_TO_LATEST;
import static com.zhuinden.xkcdexample.XkcdActions.HANDLE_DOWNLOADED_LATEST;
import static com.zhuinden.xkcdexample.XkcdActions.HANDLE_DOWNLOAD_FINISHED;
import static com.zhuinden.xkcdexample.XkcdActions.HANDLE_DOWNLOAD_STARTED;
import static com.zhuinden.xkcdexample.XkcdActions.INITIALIZE;
import static com.zhuinden.xkcdexample.XkcdActions.JUMP_TO_NUMBER;
import static com.zhuinden.xkcdexample.XkcdActions.NETWORK_ERROR;
import static com.zhuinden.xkcdexample.XkcdActions.NEXT_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_IN_BROWSER;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_JUMP_DIALOG;
import static com.zhuinden.xkcdexample.XkcdActions.OPEN_LINK;
import static com.zhuinden.xkcdexample.XkcdActions.PREVIOUS_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.RANDOM_COMIC;
import static com.zhuinden.xkcdexample.XkcdActions.SHOW_ALT_TEXT;
import static com.zhuinden.xkcdexample.XkcdActions.START_DOWNLOAD;
import static com.zhuinden.xkcdexample.XkcdActions.START_DOWNLOAD_CURRENT_OR_RETRY;
import static com.zhuinden.xkcdexample.XkcdState.current;
import static com.zhuinden.xkcdexample.XkcdState.initMax;
import static com.zhuinden.xkcdexample.XkcdState.isDownloading;
import static com.zhuinden.xkcdexample.XkcdState.max;
import static com.zhuinden.xkcdexample.XkcdState.number;
import static com.zhuinden.xkcdexample.XkcdState.putCurrent;
import static com.zhuinden.xkcdexample.XkcdState.putDownloading;
import static com.zhuinden.xkcdexample.XkcdState.putMax;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class XkcdReducer
        implements Reducer {
    @SuppressLint("NewApi")
    @Override
    public State reduce(State state, Action action) {
        switch(action.type()) {
            case START_DOWNLOAD:
            case HANDLE_DOWNLOAD_STARTED:
                return startDownload(state, action);
            case FINISH_DOWNLOAD:
            case HANDLE_DOWNLOAD_FINISHED:
                return finishDownload(state, action);
            case NEXT_COMIC:
                return nextComic(state, action);
            case PREVIOUS_COMIC:
                return previousComic(state, action);
            case RANDOM_COMIC:
                return randomComic(state, action);
            case GO_TO_LATEST:
                return goToLatest(state, action);
            case START_DOWNLOAD_CURRENT_OR_RETRY:
                return retryDownload(state, action);
            case JUMP_TO_NUMBER:
                return jumpToNumber(state, action);
            case NETWORK_ERROR:
                return networkError(state, action);
            case COMIC_SAVED:
                return comicSaved(state, action);
            case INITIALIZE:
                return initialize(state, action);
            case HANDLE_DOWNLOADED_LATEST:
                return handleDownloadedLatest(state, action);
            case XkcdActions.HANDLE_INITIALIZE_NO_DEFAULT:
                return handleInitializeNoDefault(state, action);
            case OPEN_JUMP_DIALOG:
            case OPEN_LINK:
            case SHOW_ALT_TEXT:
            case OPEN_IN_BROWSER:
            case COMIC_CHANGED:
                return createState(action, state.state());
        }
        return state;
    }

    private State handleInitializeNoDefault(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        int maxNum = initMax(action.payload());
        if(maxNum != -1) {
            stateBundle = putCurrent(stateBundle, maxNum);
            stateBundle = putMax(stateBundle, maxNum);
        }
        return createState(action, stateBundle);
    }

    private State initialize(State state, Action action) {
        return createState(action, state.state());
    }

    private State comicSaved(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        stateBundle = putCurrent(stateBundle, number(action.payload()));
        return createState(action, stateBundle);
    }

    private State networkError(State state, Action action) {
        int maxNum = initMax(action.payload());
        int max = max(state.state());
        CopyOnWriteStateBundle stateBundle = state.state();
        if(maxNum != -1 && max <= 0) {
            stateBundle = putMax(stateBundle, maxNum);
            stateBundle = putCurrent(stateBundle, maxNum);
        }
        return createState(action, stateBundle);
    }

    private State jumpToNumber(State state, Action action) {
        int number = number(action.payload());
        int max = max(state.state());
        CopyOnWriteStateBundle stateBundle = state.state();
        if(number > 0 && number <= max) {
            stateBundle = putCurrent(stateBundle, number);
        }
        return createState(action, stateBundle);
    }

    private State retryDownload(State state, Action action) {
        return state;
    }

    private State goToLatest(State state, Action action) {
        return state;
    }

    private State handleDownloadedLatest(State state, Action action) {
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

    private State finishDownload(State state, Action action) {
        return changeDownloadState(state, action, false);
    }

    private State startDownload(State state, Action action) {
        return changeDownloadState(state, action, true);
    }

    private State randomComic(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        boolean isDownloading = isDownloading(stateBundle);
        final int max = max(state.state());
        if(!isDownloading && max > 0) {
            //stateBundle = putCurrent(stateBundle, random.nextInt(max) + 1); // TODO: replace with middleware parameter
        }
        return createState(action, stateBundle);
    }

    private State previousComic(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        int current = current(stateBundle);
        boolean isDownloading = isDownloading(stateBundle);
        if(!isDownloading && current > 1) {
            stateBundle = putCurrent(stateBundle, current - 1);
        }
        return createState(action, stateBundle);
    }

    private State nextComic(State state, Action action) {
        CopyOnWriteStateBundle stateBundle = state.state();
        int current = current(stateBundle);
        boolean isDownloading = isDownloading(stateBundle);
        final int max = max(state.state());
        if(!isDownloading && current < max) {
            stateBundle = putCurrent(stateBundle, current + 1);
        }
        return createState(action, stateBundle);
    }

    private State changeDownloadState(State state, Action action, boolean isDownloading) {
        CopyOnWriteStateBundle bundle = putDownloading(state.state(), isDownloading);
        return createState(action, bundle);
    }

    private State createState(Action action, CopyOnWriteStateBundle stateBundle) {
        return State.create(stateBundle, action);
    }
}
