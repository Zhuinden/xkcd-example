package com.zhuinden.xkcdexample.redux;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

/**
 * Created by Zhuinden on 2017.04.12..
 */
@AutoValue
public abstract class Action implements Parcelable {
    public static final Action INIT = Action.create("INIT");

    public abstract String type();

    public abstract CopyOnWriteStateBundle payload();

    public static Action create(@NonNull String type) {
        return create(type, new CopyOnWriteStateBundle());
    }

    public static Action create(@NonNull String type, @NonNull CopyOnWriteStateBundle payload) {
        if(type == null) {
            throw new NullPointerException("type");
        }
        if(payload == null) {
            throw new NullPointerException("payload");
        }
        return new AutoValue_Action(type, payload);
    }
}
