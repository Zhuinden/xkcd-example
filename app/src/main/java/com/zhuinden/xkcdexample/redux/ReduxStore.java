package com.zhuinden.xkcdexample.redux;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by Zhuinden on 2017.04.12..
 */

public class ReduxStore {
    private ReduxStore(RootReducer rootReducer, List<Middleware> middlewares) {
        this.reducer = rootReducer;
        this.middlewares = Collections.unmodifiableList(middlewares);
    }

    private final State initialState = State.create(new CopyOnWriteStateBundle(), Action.INIT);

    private RootReducer reducer;
    private List<Middleware> middlewares;

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
        private LinkedList<Middleware> middlewares = new LinkedList<>();

        public Builder addReducer(Reducer reducer) {
            rootReducerBuilder.addReducer(reducer);
            return this;
        }

        public Builder addMiddleware(Middleware middleware) {
            middlewares.add(middleware);
            return this;
        }

        public ReduxStore build() {
            return new ReduxStore(rootReducerBuilder.build(), middlewares);
        }
    }

    public Flowable<StateChange> state() {
        return state.toFlowable(BackpressureStrategy.MISSING) //
                .scan(StateChange.create(initialState, initialState), //
                        (stateChange, newState) -> StateChange.create(stateChange.newState(), newState)) //
                .filter(stateChange -> stateChange.previousState() != stateChange.newState());
    }

    private Flowable<State> traverseBeforeChain(Flowable<State> stateFlowable, Action action, int index) {
        if(index >= middlewares.size()) {
            return stateFlowable;
        }
        final Middleware middleware = middlewares.get(index);
        if(stateFlowable == null) {
            stateFlowable = middleware.doBefore().intercept(this, state.getValue(), action);
        } else {
            stateFlowable = stateFlowable.concatMap(newState -> middleware.doBefore().intercept(this, newState, action));
        }
        return traverseBeforeChain(stateFlowable, action, index + 1);
    }

    private Flowable<State> traverseAfterChain(Flowable<State> stateFlowable, Action action, int index) {
        if(index < 0) {
            return stateFlowable;
        }
        final Middleware middleware = middlewares.get(index);
        if(stateFlowable == null) {
            stateFlowable = middleware.doAfter().intercept(this, state.getValue(), action);
        } else {
            stateFlowable = stateFlowable.concatMap(newState -> middleware.doAfter().intercept(this, newState, action));
        }
        return traverseAfterChain(stateFlowable, action, index - 1);
    }

    public void dispatch(Action action) {
        final State initialState = state.getValue();
        boolean hasMiddlewares = !middlewares.isEmpty();
        if(!hasMiddlewares) {
            reducer.reduce(initialState, action) //
                    .concatMap((newState) -> Flowable.just(newState)) //
                    .doOnNext(newState -> state.accept(newState)) //
                    .subscribe();
        } else {
            Flowable<State> stateFlowable = traverseBeforeChain(null, action, 0);
            stateFlowable = stateFlowable.concatMap((state) -> reducer.reduce(state, action));
            traverseAfterChain(stateFlowable,
                    action,
                    middlewares.size() - 1) // TODO: "before" is executed only for initial, but "after" is executed for each emitted action! Maybe emission should be Single after all!
                    .concatMap(newState -> Flowable.just(newState))
                    .doOnNext(newState -> state.accept(newState)) //
                    .subscribe();
        }
    }
}
