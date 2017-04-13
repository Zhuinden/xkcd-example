package com.zhuinden.xkcdexample;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.zhuinden.statebundle.StateBundle;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class ReduxStore {
    BehaviorRelay<State> state = BehaviorRelay.createDefault(State.create(new StateBundle(), Action.INIT));

    RootReducer reducer = RootReducer.builder().addReducer(new XkcdReducer()).build();

    public Observable<State> state() {
        return state;
    }

    public Single<StateChange> dispatch(Action action) {
        final State previousState = state.getValue();
        Single<State> _newState = reducer.reduce(state.getValue(), action);
        return _newState.map(newState -> StateChange.create(previousState, newState));
    }
}
