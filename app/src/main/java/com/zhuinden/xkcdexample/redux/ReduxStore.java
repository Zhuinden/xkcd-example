package com.zhuinden.xkcdexample.redux;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class ReduxStore {
    private ReduxStore(RootReducer rootReducer) {
        this.reducer = rootReducer;
    }

    private final State initialState = State.create(new CopyOnWriteStateBundle(), Action.INIT);

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

    public Flowable<StateChange> state() {
        return state.toFlowable(BackpressureStrategy.MISSING) //
                .scan(StateChange.create(initialState, initialState), //
                        (stateChange, newState) -> StateChange.create(stateChange.newState(), newState)) //
                .filter(stateChange -> stateChange.previousState() != stateChange.newState());
    }

    public void dispatch(Action action) {
        final State currentState = state.getValue();
        reducer.reduce(currentState, action) //
                .concatMap((newState) -> Flowable.just(newState)) //
                .doOnNext(newState -> state.accept(newState)) //
                .subscribe();
    }
}
