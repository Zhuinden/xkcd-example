package com.zhuinden.xkcdexample.redux;

/**
 * Created by Owner on 2017. 04. 19..
 */

public interface Middleware {
    Reducer doBefore();

    Reducer doAfter();
}
