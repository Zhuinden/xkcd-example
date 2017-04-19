package com.zhuinden.xkcdexample.redux;

/**
 * Created by Owner on 2017. 04. 13..
 */

public interface Reducer {
    State reduce(final State state, final Action action);
}
