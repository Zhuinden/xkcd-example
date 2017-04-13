package com.zhuinden.xkcdexample;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by Owner on 2017. 04. 13..
 */

@AutoValue
public abstract class StateChange
        implements Parcelable {
    public abstract State previousState();

    public abstract State newState();

    public static StateChange create(State previousState, State newState) {
        return new AutoValue_StateChange(previousState, newState);
    }
}
