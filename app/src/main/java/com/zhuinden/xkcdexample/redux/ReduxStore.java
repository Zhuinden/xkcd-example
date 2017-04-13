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

    private RootReducer reducer;

    BehaviorRelay<State> state = BehaviorRelay.createDefault(initialState);

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
        return state.scan(StateChange.create(initialState, initialState),
                (stateChange, newState) -> StateChange.create(stateChange.newState(), newState));
    }

    public Single<StateChange> dispatch(Action action) {
        final State currentState = state.getValue();
        Single<State> _newState = reducer.reduce(state.getValue(), action);
        return _newState.map(newState -> StateChange.create(currentState, newState))
                .doOnSuccess(stateChange -> state.accept(stateChange.newState()));
    }
}
