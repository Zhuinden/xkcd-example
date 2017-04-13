package com.zhuinden.xkcdexample;

import com.zhuinden.statebundle.StateBundle;

/**
 * Created by Owner on 2017. 04. 13..
 */

public class XkcdState {
    public static int max(StateBundle stateBundle) {
        return stateBundle.getInt("max", 0);
    }

    public static void putMax(StateBundle stateBundle, int max) {
        stateBundle.putInt("max", max);
    }

    public static int current(StateBundle stateBundle) {
        return stateBundle.getInt("current", 0);
    }

    public static void putCurrent(StateBundle stateBundle, int current) {
        stateBundle.putInt("current", current);
    }
}
