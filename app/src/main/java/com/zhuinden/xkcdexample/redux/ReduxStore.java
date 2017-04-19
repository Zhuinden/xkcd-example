package com.zhuinden.xkcdexample.redux;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

import org.javatuples.Pair;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

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
            throw new IllegalStateException(
                    "Initial state cannot be set after internal state has already been modified!");
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

    private Single<Pair<State, Action>> traverseBeforeChain(Single<Pair<State, Action>> stateSingle, Action action, int index) {
        if(index >= middlewares.size()) {
            return stateSingle;
        }
        final Middleware middleware = middlewares.get(index);
        if(stateSingle == null) {
            stateSingle = middleware.executeBefore(this, state.getValue(), action);
        } else {
            stateSingle = stateSingle.flatMap(newState -> middleware.executeBefore(this,
                    newState.getValue0(),
                    newState.getValue1()));
        }
        return traverseBeforeChain(stateSingle, action, index + 1);
    }

    private Single<Pair<State, Action>> traverseAfterChain(Single<Pair<State, Action>> stateSingle, Action action, int index) {
        if(index < 0) {
            return stateSingle;
        }
        final Middleware middleware = middlewares.get(index);
        if(stateSingle == null) {
            stateSingle = middleware.executeAfter(this, state.getValue(), action);
        } else {
            stateSingle = stateSingle.flatMap(newState -> middleware.executeAfter(this,
                    newState.getValue0(),
                    newState.getValue1()));
        }
        return traverseAfterChain(stateSingle, action, index - 1);
    }

    public void dispatch(Action action) {
        final State initialState = state.getValue();
        boolean hasMiddlewares = !middlewares.isEmpty();
        if(!hasMiddlewares) {
            Single.just(reducer.reduce(initialState, action)).doOnSuccess(newState -> state.accept(newState)) //
                    .subscribe();
        } else {
            Single<Pair<State, Action>> stateSingle = traverseBeforeChain(null, action, 0);
            stateSingle = stateSingle.map((state) -> //
                    Pair.with(reducer.reduce(state.getValue0(), state.getValue1()), state.getValue1()))
                    .doOnSuccess(newState -> state.accept(newState.getValue0()));
            traverseAfterChain(stateSingle, action, middlewares.size() - 1).subscribe();
        }
    }
}
