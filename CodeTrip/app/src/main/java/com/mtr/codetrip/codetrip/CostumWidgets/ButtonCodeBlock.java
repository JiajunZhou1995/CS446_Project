package com.mtr.codetrip.codetrip.CostumWidgets;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.mtr.codetrip.codetrip.Object.Question;
import com.mtr.codetrip.codetrip.Object.QuestionDragAndDrop;
import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.DensityUtil;

/**
 * Created by Catrina on 2/26/2018 at 11:29 PM.
 * Within Package ${PACKAGE_NAME}
 */

@SuppressLint("ViewConstructor")
public class ButtonCodeBlock extends AppCompatButton implements View.OnTouchListener, View.OnDragListener {

    private Question question;

    public ButtonCodeBlock(Context context, String codeBlock, int tag, Question question) {
        super(context);
        this.setTag(tag);
        this.question = question;
        this.setText(codeBlock);
        this.setMinWidth(DensityUtil.dip2px(context, 25));
        this.setMinimumWidth(DensityUtil.dip2px(context, 25));
        this.setPadding(DensityUtil.dip2px(context, 20), 0, DensityUtil.dip2px(context, 20), 0);
        this.setAllCaps(false);
        this.setBackground(context.getDrawable(R.drawable.drop_block_round_enabled));
        this.setTextAppearance(R.style.FontStyle_CodeBlock);

        this.setOnTouchListener(this);
        this.setOnDragListener(this);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);

        // 剪切板数据，可以在DragEvent.ACTION_DROP方法的时候获取。
        ClipData data = ClipData.newPlainText("dot", "Dot : " + view.toString());
        // 开始拖拽
        ((QuestionDragAndDrop) question).currentOnDragButton = (Button) view;
        ((QuestionDragAndDrop) question).currentOnDragButtonText = (String) ((Button) view).getText();
        view.startDrag(data, builder, view, 0);
        return true;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        final int action = dragEvent.getAction();
        switch (action) {
            // 开始拖拽
            case DragEvent.ACTION_DRAG_STARTED:
                if (view == ((QuestionDragAndDrop) question).currentOnDragButton)
                    view.setVisibility(View.INVISIBLE);
                break;
            // 结束拖拽
            case DragEvent.ACTION_DRAG_ENDED:
                ((QuestionDragAndDrop) question).dropReceiveBlank.checkContains((Button) view);
                break;
            // 拖拽进某个控件后，退出
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            // 拖拽进某个控件后，保持
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            // 推拽进入某个控件
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            // 推拽进入某个控件，后在该控件内，释放。即把推拽控件放入另一个控件
            case DragEvent.ACTION_DROP:
                break;
        }
        return true;
    }
}
