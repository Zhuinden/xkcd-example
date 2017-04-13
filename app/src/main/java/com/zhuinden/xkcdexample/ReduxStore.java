package com.zhuinden.xkcdexample;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.zhuinden.statebundle.StateBundle;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class ReduxStore {
    private State previousState = State.create(new StateBundle(), Action.INIT);

    BehaviorRelay<State> state = BehaviorRelay.createDefault(previousState);

    RootReducer reducer = RootReducer.builder().addReducer(new XkcdReducer()).build();

    public Observable<StateChange> state() {
        return state.map(currentState -> StateChange.create(previousState, currentState));
    }

    public Single<StateChange> dispatch(Action action) {
        final State currentState = state.getValue();
        Single<State> _newState = reducer.reduce(state.getValue(), action);
        return _newState.map(newState -> StateChange.create(currentState, newState))
                .doOnSuccess(stateChange -> previousState = stateChange.previousState())
                .doOnSuccess(stateChange -> state.accept(stateChange.newState()));
    }
}
