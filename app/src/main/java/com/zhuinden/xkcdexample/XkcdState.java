package com.zhuinden.xkcdexample;

import com.zhuinden.xkcdexample.util.CopyOnWriteStateBundle;

/**
 * Created by Owner on 2017. 04. 13..
 */

public class XkcdState {
    private static final String MAX = "max";
    private static final String CURRENT = "current";
    private static final String IS_DOWNLOADING = "isDownloading";
    private static final String LINK = "link";
    private static final String ALT_TEXT = "altText";
    private static final String NUMBER = "number";
    private static final String INIT_MAX = "initMax";


    public static int max(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt(MAX, 0);
    }

    public static CopyOnWriteStateBundle putMax(CopyOnWriteStateBundle stateBundle, int max) {
        return (CopyOnWriteStateBundle) stateBundle.putInt(MAX, max);
    }

    public static int current(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt(CURRENT, 0);
    }

    public static CopyOnWriteStateBundle putCurrent(CopyOnWriteStateBundle stateBundle, int current) {
        return (CopyOnWriteStateBundle) stateBundle.putInt(CURRENT, current);
    }

    public static boolean isDownloading(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getBoolean(IS_DOWNLOADING, false);
    }

    public static CopyOnWriteStateBundle putDownloading(CopyOnWriteStateBundle stateBundle, boolean isDownloading) {
        return (CopyOnWriteStateBundle) stateBundle.putBoolean(IS_DOWNLOADING, isDownloading);
    }

    public static String link(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getString(LINK);
    }

    public static CopyOnWriteStateBundle putLink(CopyOnWriteStateBundle stateBundle, String link) {
        return (CopyOnWriteStateBundle) stateBundle.putString(LINK, link);
    }

    public static String altText(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getString("ALT_TEXT");
    }

    public static CopyOnWriteStateBundle putAltText(CopyOnWriteStateBundle stateBundle, String alt) {
        return (CopyOnWriteStateBundle) stateBundle.putString("ALT_TEXT", alt);
    }

    public static int number(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt(NUMBER);
    }

    public static CopyOnWriteStateBundle putNumber(CopyOnWriteStateBundle stateBundle, int number) {
        return (CopyOnWriteStateBundle) stateBundle.putInt(NUMBER, number);
    }

    public static int initMax(CopyOnWriteStateBundle stateBundle) {
        return stateBundle.getInt(INIT_MAX, 0);
    }

    public static CopyOnWriteStateBundle putInitMax(CopyOnWriteStateBundle stateBundle, int max) {
        return (CopyOnWriteStateBundle) stateBundle.putInt(INIT_MAX, max);
    }
}
