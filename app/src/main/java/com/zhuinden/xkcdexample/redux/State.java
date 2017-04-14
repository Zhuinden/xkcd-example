package com.zhuinden.xkcdexample.redux;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

/**
 * Created by Zhuinden on 2017.04.12..
 */
@AutoValue
public abstract class State
        implements Parcelable {
    public abstract CopyOnWriteStateBundle state();

    public abstract Action action();

    public static State create(CopyOnWriteStateBundle state, Action previousAction) {
        return new AutoValue_State(state, previousAction);
    }
}
