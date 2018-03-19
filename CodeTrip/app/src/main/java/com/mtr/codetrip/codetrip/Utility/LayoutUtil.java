package com.mtr.codetrip.codetrip.Utility;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Catrina on 2/26/2018 at 12:22 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class LayoutUtil {

    public enum LayoutType {LINEAR, RELATIVE}

    public enum ParamType {MATCH_PARENT, WRAP_CONTENT}


    public static void setup(Context context, LayoutType type, View targetView, ParamType width, ParamType height, int marginLeft, int marginTop, int marginRight, int marginBottom) {

        switch (type) {
            case LINEAR:
                setUpLinearLayout(context, targetView, width, height, marginLeft, marginTop, marginRight, marginBottom);
                break;
            case RELATIVE:
                setUpRelativeLayout(context, targetView, width, height, marginLeft, marginTop, marginRight, marginBottom);
                break;
        }
    }

    private static void setUpLinearLayout(Context context, View targetView, ParamType width, ParamType height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (width == ParamType.MATCH_PARENT) ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
                (height == ParamType.MATCH_PARENT) ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DensityUtil.dip2px(context, marginLeft),
                DensityUtil.dip2px(context, marginTop),
                DensityUtil.dip2px(context, marginRight),
                DensityUtil.dip2px(context, marginBottom));
        targetView.setLayoutParams(layoutParams);
    }

    private static void setUpRelativeLayout(Context context, View targetView, ParamType width, ParamType height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (width == ParamType.MATCH_PARENT) ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
                (height == ParamType.MATCH_PARENT) ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DensityUtil.dip2px(context, marginLeft),
                DensityUtil.dip2px(context, marginTop),
                DensityUtil.dip2px(context, marginRight),
                DensityUtil.dip2px(context, marginBottom));
        targetView.setLayoutParams(layoutParams);
    }
}
