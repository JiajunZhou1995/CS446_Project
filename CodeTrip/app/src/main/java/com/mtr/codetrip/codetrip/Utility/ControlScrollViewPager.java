package com.mtr.codetrip.codetrip.Utility;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mtr.codetrip.codetrip.QuestionActivity;

/**
 * Created by Catrina on 27/02/2018 at 12:15 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class ControlScrollViewPager extends ViewPager {


    /**
     * 上一次x坐标
     */
    private float beforeX;
    private QuestionActivity questionActivity = null;


    public ControlScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ControlScrollViewPager(Context context) {

        super(context);
    }

//    private boolean isCanScroll = true;

    //----------禁止左右滑动------------------
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (isCanScroll) {
//            return super.onTouchEvent(ev);
//        } else {
//            return false;
//        }
//
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        // TODO Auto-generated method stub
//        if (isCanScroll) {
//            return super.onInterceptTouchEvent(arg0);
//        } else {
//            return false;
//        }
//
//    }

//-------------------------------------------

    public void setBoundedQuestionActivity(QuestionActivity questionActivity) {
        this.questionActivity = questionActivity;
    }

    //-----禁止左滑-------左滑：上一次坐标 > 当前坐标
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.getCurrentItem() < questionActivity.currentProgress) {
            return super.dispatchTouchEvent(ev);
        } else {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                    beforeX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (motionValue < 0) {//禁止左滑
                        return true;
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题

                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }
    }

}
