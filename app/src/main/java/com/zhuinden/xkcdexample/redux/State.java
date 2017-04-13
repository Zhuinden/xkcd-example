package com.zhuinden.xkcdexample.redux;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.zhuinden.statebundle.StateBundle;

/**
 * Created by Zhuinden on 2017.04.12..
 */
@AutoValue
public abstract class State
        implements Parcelable {
    public abstract StateBundle state();

    public abstract Action previousAction();

    public static State create(StateBundle state, Action previousAction) {
        return new AutoValue_State(state, previousAction);
    }
}
