package com.zhuinden.xkcdexample;

import com.google.auto.value.AutoValue;
import com.zhuinden.statebundle.StateBundle;

/**
 * Created by Zhuinden on 2017.04.12..
 */
@AutoValue
public abstract class State {
    public abstract StateBundle currentState();

    public abstract Action previousAction();
}
