package com.zhuinden.xkcdexample;

import com.google.auto.value.AutoValue;
import com.zhuinden.statebundle.StateBundle;

/**
 * Created by Zhuinden on 2017.04.12..
 */
@AutoValue
public abstract class Action {
    public abstract String type();

    public abstract StateBundle payload();

    public static Action create(String type) {
        return create(type, new StateBundle());
    }

    public static Action create(String type, StateBundle payload) {
        return new AutoValue_Action(type, payload);
    }
}
