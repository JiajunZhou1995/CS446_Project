package com.mtr.codetrip.codetrip.helper;

/**
 * Created by Catrina on 25/02/2018.
 */

public class MultipleClickUtility {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
