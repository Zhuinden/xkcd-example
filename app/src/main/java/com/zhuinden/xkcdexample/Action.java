package com.zhuinden.xkcdexample;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.zhuinden.statebundle.StateBundle;

/**
 * Created by Zhuinden on 2017.04.12..
 */
@AutoValue
public abstract class Action implements Parcelable {
    public static final Action INIT = Action.create("INIT");

    public abstract String type();

    public abstract StateBundle payload();

    public static Action create(@NonNull String type) {
        return create(type, new StateBundle());
    }

    public static Action create(@NonNull String type, @NonNull StateBundle payload) {
        if(type == null) {
            throw new NullPointerException("type");
        }
        if(payload == null) {
            throw new NullPointerException("payload");
        }
        return new AutoValue_Action(type, payload);
    }
}
