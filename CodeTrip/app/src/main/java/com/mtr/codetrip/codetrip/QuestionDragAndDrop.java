package com.mtr.codetrip.codetrip;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.database.Cursor;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Catrina on 24/02/2018.
 */

public class QuestionDragAndDrop extends Question {
    private List<String> codeArea;
    private List<String> codeBlocks;


    public QuestionDragAndDrop(ViewGroup viewGroup){
        super(viewGroup);
    }

    @Override
    public void populateFromDB(Cursor c){
        super.populateFromDB(c);

        codeArea =  getArrayFromDB(c, "code");
        codeBlocks = getArrayFromDB(c, "codeblock");
    }

    private void setUpView(View view,int width,int height, int marginLeft, int marginTop, int marginRight, int marginBottom){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (width==0)?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT,
                (height==0)?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DensityUtil.dip2px(context,marginLeft),
                                DensityUtil.dip2px(context,marginTop),
                                DensityUtil.dip2px(context,marginRight),
                                DensityUtil.dip2px(context,marginBottom));
        view.setLayoutParams(layoutParams);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        LinearLayout questionContent = rootView.findViewById(R.id.question_content);
        View dragAndDrop = layoutInflater.inflate(R.layout.question_drag_and_drop,null);
        questionContent.addView(dragAndDrop);


        View action_menu = layoutInflater.inflate(R.layout.stars_indicator,null);

        LinearLayout.LayoutParams layoutParams = null;
        LinearLayout codeAreaLinearLayout = (LinearLayout) questionContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine = null;
        int lineIndex = 1;
        for (String codeLine : codeArea){
            String[] code = codeLine.split("(\\[\\?\\])");
            singleLine = (LinearLayout) layoutInflater.inflate(R.layout.question_code_area_single_line,null);

            TextView lineNumberTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_line_number_textview,null);
            setUpView(lineNumberTextView,1,1,15,0,5,0);
            lineNumberTextView.setText(String.format("%d.",lineIndex));
            singleLine.addView(lineNumberTextView);


            int index = 0;
            for (String s :code){
                TextView normalTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_normal_textview,null);
                setUpView(normalTextView,1,1,0,0,0,0);
                normalTextView.setText(s);
                singleLine.addView(normalTextView);

                if (index++ < code.length -1){
                    TextView specialTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_drop_textview,null);
                    setUpView(specialTextView,1,1,5,0,5,0);
                    specialTextView.setText("?");
                    singleLine.addView(specialTextView);
                }
            }

            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }

        LinearLayout codeBlockArea = (LinearLayout) questionContent.findViewById(R.id.question_code_block_area);
        for (String codeBlockText : codeBlocks){
            Button codeBlockButton = (Button) layoutInflater.inflate(R.layout.question_code_block_button,null);
            setUpView(codeBlockButton,1,1,15,0,15,0);
            codeBlockButton.setText(codeBlockText);


//            codeBlockButton.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
//                    // 剪切板数据，可以在DragEvent.ACTION_DROP方法的时候获取。
//                    ClipData data = ClipData.newPlainText("dot", "Dot : " + view.toString());
//                    // 开始拖拽
//                    view.startDrag(data, builder, view, 0);
//                    return true;
//                }
//            });

            codeBlockButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
                    // 剪切板数据，可以在DragEvent.ACTION_DROP方法的时候获取。
                    ClipData data = ClipData.newPlainText("dot", "Dot : " + view.toString());
                    // 开始拖拽
                    view.startDrag(data, builder, view, 0);
                    return true;
                }

            });

            codeBlockButton.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    String TAG = "code block button";
                    final int action = dragEvent.getAction();
                    switch (action) {
                        // 开始拖拽
                        case DragEvent.ACTION_DRAG_STARTED:
                            view.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "image1 ACTION_DRAG_STARTED");
                            break;
                        // 结束拖拽
                        case DragEvent.ACTION_DRAG_ENDED:
                            view.setVisibility(View.VISIBLE);
                            Log.d(TAG, "image1 ACTION_DRAG_ENDED");
                            break;
                        // 拖拽进某个控件后，退出
                        case DragEvent.ACTION_DRAG_EXITED:
                            Log.d(TAG, "image1 ACTION_DRAG_EXITED");
                            break;
                        // 拖拽进某个控件后，保持
                        case DragEvent.ACTION_DRAG_LOCATION:
                            Log.d(TAG, "image1 ACTION_DRAG_LOCATION");
                            break;
                        // 推拽进入某个控件
                        case DragEvent.ACTION_DRAG_ENTERED:
                            Log.d(TAG, "image1 ACTION_DRAG_ENTERED");
                            break;
                        // 推拽进入某个控件，后在该控件内，释放。即把推拽控件放入另一个控件
                        case DragEvent.ACTION_DROP:
                            Log.d(TAG, "image1 ACTION_DROP");
                            break;
                    }
                    return true;
                }
            });
            codeBlockArea.addView(codeBlockButton);
        }
    }

}
