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

    public static boolean isDownloading(StateBundle stateBundle) {
        return stateBundle.getBoolean("isDownloading", false);
    }

    public static void putDownloading(StateBundle stateBundle, boolean isDownloading) {
        stateBundle.putBoolean("isDownloading", isDownloading);
    }

    public static String link(StateBundle stateBundle) {
        return stateBundle.getString("link");
    }

    public static void putLink(StateBundle stateBundle, String link) {
        stateBundle.putString("link", link);
    }

    public static String altText(StateBundle stateBundle) {
        return stateBundle.getString("altText");
    }

    public static void putAltText(StateBundle stateBundle, String alt) {
        stateBundle.putString("altText", alt);
    }

    public static int number(StateBundle stateBundle) {
        return stateBundle.getInt("number");
    }

    public static void putNumber(StateBundle stateBundle, int number) {
        stateBundle.putInt("number", number);
    }
}
