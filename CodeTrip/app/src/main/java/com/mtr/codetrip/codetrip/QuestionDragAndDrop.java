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

import com.mtr.codetrip.codetrip.helper.DropTextView;
import com.mtr.codetrip.codetrip.helper.FillInTheBlank;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Catrina on 24/02/2018.
 */

public class QuestionDragAndDrop extends Question {
    private List<String> codeArea;
    private List<String> codeBlocks;
    private String currentOnDragButtonText;
    private Button currenOnDragButton;
    private List<Button> codeBlockButtonList;
    public FillInTheBlank fillInTheBlank;
    public Button doitButon;


    public QuestionDragAndDrop(ViewGroup viewGroup){
        super(viewGroup);
        codeBlockButtonList = new ArrayList<>();
        doitButon = rootView.findViewById(R.id.doit);
        fillInTheBlank = new FillInTheBlank(context,doitButon);
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


            int index = 0, blankIndex= 0;
            for (String s :code){
                TextView normalTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_normal_textview,null);
                setUpView(normalTextView,1,1,0,0,0,0);
                normalTextView.setText(s);
                singleLine.addView(normalTextView);

                if (index++ < code.length -1){

                    DropTextView dropTextView = new DropTextView(context);

//                    DropTextView dropTextView = (DropTextView)  layoutInflater.inflate(R.layout.question_code_area_drop_textview,null);
                    setUpView(dropTextView,1,1,5,0,5,0);
                    dropTextView.setTag(blankIndex);
                    dropTextView.setDropState(DropTextView.DropState.DEFAULT);
                    fillInTheBlank.addEntry();
                    singleLine.addView(dropTextView);
                    dropTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DropTextView textView = (DropTextView) v;
                            fillInTheBlank.restore(textView);
                        }
                    });
                    dropTextView.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View v, DragEvent event) {
                            DropTextView textView = (DropTextView) v;
                            final int action = event.getAction();
                            switch (action) {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    break;
                                case DragEvent.ACTION_DRAG_ENDED:
                                    break;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    textView.setDropState(DropTextView.DropState.DEFAULT);
                                    break;
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    textView.setDropState(DropTextView.DropState.ENABLED);
                                    break;
                                case DragEvent.ACTION_DRAG_LOCATION:
                                    break;
                                case DragEvent.ACTION_DROP:
                                    textView.setDropState(DropTextView.DropState.DROPPED);
                                    textView.setText(currentOnDragButtonText);
                                    fillInTheBlank.updateFillIn((int)v.getTag(),currenOnDragButton);
                                    break;
                            }
                            return true;
                        }
                    });
                    blankIndex++;
                }
            }

            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }

        int index = 0;
        LinearLayout codeBlockArea = (LinearLayout) questionContent.findViewById(R.id.question_code_block_area);
        for (String codeBlockText : codeBlocks){
            Button codeBlockButton = (Button) layoutInflater.inflate(R.layout.question_code_block_button,null);
            setUpView(codeBlockButton,1,1,15,0,15,0);
            codeBlockButton.setText(codeBlockText);
            codeBlockButton.setTag(index);
            codeBlockButtonList.add(codeBlockButton);


            codeBlockButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
                    // 剪切板数据，可以在DragEvent.ACTION_DROP方法的时候获取。
                    ClipData data = ClipData.newPlainText("dot", "Dot : " + view.toString());
                    // 开始拖拽
                    currenOnDragButton = (Button)view;
                    currentOnDragButtonText = (String) ((Button) view).getText();
                    Log.d("Set current", currentOnDragButtonText);
                    view.startDrag(data, builder, view, 0);
                    return true;
                }

            });

            codeBlockButton.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    final int action = dragEvent.getAction();
                    switch (action) {
                        // 开始拖拽
                        case DragEvent.ACTION_DRAG_STARTED:
                            if ((Button)view == currenOnDragButton) view.setVisibility(View.INVISIBLE);
                            break;
                        // 结束拖拽
                        case DragEvent.ACTION_DRAG_ENDED:
//                            if (!blankSpaceList.contains(view)) view.setVisibility(View.VISIBLE);
                            fillInTheBlank.checkContains((Button) view);
                            break;
                        // 拖拽进某个控件后，退出
                        case DragEvent.ACTION_DRAG_EXITED:
//                            Log.d(TAG, "image1 ACTION_DRAG_EXITED");
                            break;
                        // 拖拽进某个控件后，保持
                        case DragEvent.ACTION_DRAG_LOCATION:
//                            Log.d(TAG, "image1 ACTION_DRAG_LOCATION");
                            break;
                        // 推拽进入某个控件
                        case DragEvent.ACTION_DRAG_ENTERED:
//                            Log.d(TAG, "image1 ACTION_DRAG_ENTERED");
                            break;
                        // 推拽进入某个控件，后在该控件内，释放。即把推拽控件放入另一个控件
                        case DragEvent.ACTION_DROP:
//                            Log.d(TAG, "image1 ACTION_DROP");
                            break;
                    }
                    return true;
                }
            });
            codeBlockArea.addView(codeBlockButton);
            index ++;
        }
    }

}
