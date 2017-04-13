package com.zhuinden.xkcdexample.redux;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.zhuinden.statebundle.StateBundle;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class ReduxStore {
    private ReduxStore(RootReducer rootReducer) {
        this.reducer = rootReducer;
    }

    private final State initialState = State.create(new StateBundle(), Action.INIT);

    private State previousState = initialState;

    private RootReducer reducer;

    BehaviorRelay<State> state = BehaviorRelay.createDefault(initialState);

    public State getState() {
        return state.getValue();
    }

    public boolean isAtInitialState() {
        return this.state.getValue() == initialState;
    }

    public void setInitialState(State state) {
        if(this.state.getValue() != initialState) {
            throw new IllegalStateException("Initial state cannot be set after internal state has already been modified!");
        }
        this.state.accept(state);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Builder() {
        }

        private RootReducer.Builder rootReducerBuilder = RootReducer.builder();

        public Builder addReducer(Reducer reducer) {
            rootReducerBuilder.addReducer(reducer);
            return this;
        }

        public ReduxStore build() {
            return new ReduxStore(rootReducerBuilder.build());
        }
    }

    public Observable<StateChange> state() {
        return state.map(currentState -> StateChange.create(previousState,
                currentState)); // TODO: figure out how to make this work with Scan()
    }

    public Single<StateChange> dispatch(Action action) {
        final State currentState = state.getValue();
        Single<State> _newState = reducer.reduce(state.getValue(), action);
        return _newState.map(newState -> StateChange.create(currentState, newState))
                .doOnSuccess(stateChange -> previousState = stateChange.previousState())
                .doOnSuccess(stateChange -> state.accept(stateChange.newState()));
    }
}
