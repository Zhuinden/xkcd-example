package com.zhuinden.xkcdexample.redux;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public State reduce(final State state, final Action action) {
        for(Reducer reducer : reducers) {
            State _state = reducer.reduce(state, action);
            if(_state != state) {
                return _state;
            }
        }
        return state;
    }
}
