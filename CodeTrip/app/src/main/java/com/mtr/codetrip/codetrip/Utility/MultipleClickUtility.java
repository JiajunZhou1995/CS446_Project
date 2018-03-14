package com.mtr.codetrip.codetrip.Utility;

/**
 * Created by Catrina on 25/02/2018 at 12:26 AM.
 * Within Package: ${PACKAGE_NAME}
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
