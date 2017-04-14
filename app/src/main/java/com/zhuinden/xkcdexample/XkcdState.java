package com.zhuinden.xkcdexample;

import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

/**
 * Created by Owner on 2017. 04. 13..
 */

public class XkcdState {
    public static int max(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt("max", 0);
    }

    public static CopyOnWriteStateBundle putMax(CopyOnWriteStateBundle stateBundle, int max) {
        return (CopyOnWriteStateBundle) stateBundle.putInt("max", max);
    }

    public static int current(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt("current", 0);
    }

    public static CopyOnWriteStateBundle putCurrent(CopyOnWriteStateBundle stateBundle, int current) {
        return (CopyOnWriteStateBundle) stateBundle.putInt("current", current);
    }

    public static boolean isDownloading(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getBoolean("isDownloading", false);
    }

    public static CopyOnWriteStateBundle putDownloading(CopyOnWriteStateBundle stateBundle, boolean isDownloading) {
        return (CopyOnWriteStateBundle) stateBundle.putBoolean("isDownloading", isDownloading);
    }

    public static String link(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getString("link");
    }

    public static CopyOnWriteStateBundle putLink(CopyOnWriteStateBundle stateBundle, String link) {
        return (CopyOnWriteStateBundle) stateBundle.putString("link", link);
    }

    public static String altText(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getString("altText");
    }

    public static CopyOnWriteStateBundle putAltText(CopyOnWriteStateBundle stateBundle, String alt) {
        return (CopyOnWriteStateBundle) stateBundle.putString("altText", alt);
    }

    public static int number(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt("number");
    }

    public static CopyOnWriteStateBundle putNumber(CopyOnWriteStateBundle stateBundle, int number) {
        return (CopyOnWriteStateBundle) stateBundle.putInt("number", number);
    }

    public static int initMax(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt("initMax", 0);
    }

    public static CopyOnWriteStateBundle putInitMax(CopyOnWriteStateBundle stateBundle, int max) {
        return (CopyOnWriteStateBundle) stateBundle.putInt("initMax", max);
    }
}
