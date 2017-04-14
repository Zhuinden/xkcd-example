package com.zhuinden.xkcdexample.redux;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Owner on 2017. 04. 13..
 */

public class RootReducer
        implements Reducer {
    private List<Reducer> reducers;

    private RootReducer(List<Reducer> reducers) {
        this.reducers = reducers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Builder() {
        }

        List<Reducer> reducers = new LinkedList<>();

        public Builder addReducer(Reducer reducer) {
            reducers.add(reducer);
            return this;
        }

        public RootReducer build() {
            return new RootReducer(Collections.unmodifiableList(reducers));
        }
    }

    public Observable<State> reduce(final State state, final Action action) {
        List<Observable<State>> states = new LinkedList<>();
        for(Reducer reducer : reducers) {
            states.add(reducer.reduce(state, action));
        }
        return Observable.zip(states, objects -> {
            for(Object object : objects) {
                State newState = (State) object;
                if(newState != state) {
                    return newState;
                }
            }
            return state;
        });
    }
}
