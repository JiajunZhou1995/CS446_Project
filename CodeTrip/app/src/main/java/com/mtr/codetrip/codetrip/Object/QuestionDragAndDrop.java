package com.mtr.codetrip.codetrip.Object;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.AsyncResponse;
import com.mtr.codetrip.codetrip.CostumWidgets.ButtonCodeBlock;
import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewDropBlank;
import com.mtr.codetrip.codetrip.Utility.DropReceiveBlank;
import com.mtr.codetrip.codetrip.Utility.HttpPostAsyncTask;
import com.mtr.codetrip.codetrip.Utility.LayoutUtil;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewLineNumber;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewNormalCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 24/02/2018 at 11:46 PM.
 * Within Package: ${PACKAGE_NAME}
 */

public class QuestionDragAndDrop extends Question implements AsyncResponse {
    private List<String> codeArea;
    private List<String> codeBlocks;

    public String currentOnDragButtonText;
    public Button currentOnDragButton;

    private List<Button> codeBlockButtonList;
    public DropReceiveBlank dropReceiveBlank;
    private List<List<TextView>> normalCode;
    private String codeString;
    //drop TextView List
    private List<List<TextViewDropBlank>> textViewDropBlankList;
    private QuestionDragAndDrop thisQuestionView;

    QuestionDragAndDrop(ViewGroup viewGroup){
        super(viewGroup);
        thisQuestionView = this;
        codeString = "";
        normalCode = new ArrayList<>();
        codeBlockButtonList = new ArrayList<>();
        textViewDropBlankList = new ArrayList<>();
        RunButton runButton = rootView.findViewById(R.id.doit);
        dropReceiveBlank = new DropReceiveBlank(runButton);
    }




    @Override
    public void populateFromDB(Cursor c){
        super.populateFromDB(c);

        codeArea =  getArrayFromDB(c, "code");
        codeBlocks = getArrayFromDB(c, "codeblock");
    }


    private void inflateCodeArea(View dragAndDropContent){
        Context currentContext = dragAndDropContent.getContext();
//        LayoutInflater layoutInflater = LayoutInflater.from(currentContext);

        LinearLayout codeAreaLinearLayout = dragAndDropContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine;
        List<TextView>normalCodeSingleLine;
        List<TextViewDropBlank>dropReceiveBlankSingLine;
        List<Button> dropReceiveRecordSingleLine;
        int lineIndex = 0;
        for (String codeLine : codeArea){
            normalCodeSingleLine = new ArrayList<>();
            dropReceiveRecordSingleLine = new ArrayList<>();
            dropReceiveBlankSingLine = new ArrayList<>();
            String[] code = codeLine.split("(\\[\\?])");
            singleLine = new LinearLayout(currentContext);
            singleLine.setOrientation(LinearLayout.HORIZONTAL);
            LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR,singleLine, LayoutUtil.ParamType.MATCH_PARENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);


            TextViewLineNumber textViewLineNumber = new TextViewLineNumber(currentContext,Integer.toString(lineIndex+1)+".");
            LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, textViewLineNumber, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,15,0,5,0);
            singleLine.addView(textViewLineNumber);


            int index = 0, blankIndex= 0;
            for (String s :code){
                TextViewNormalCode normalTextView = new TextViewNormalCode(currentContext, s);
                LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, normalTextView, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,2,0,2,0);
                singleLine.addView(normalTextView);
                normalCodeSingleLine.add(normalTextView);

                if (index++ < code.length -1){
                    TextViewDropBlank textViewDropBlank = new TextViewDropBlank(context, lineIndex, blankIndex,this);
                    LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, textViewDropBlank, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);
                    textViewDropBlank.updateDropState(TextViewDropBlank.DropState.DEFAULT);
                    dropReceiveRecordSingleLine.add(null);
                    dropReceiveBlankSingLine.add(textViewDropBlank);
                    singleLine.addView(textViewDropBlank);
                    blankIndex++;
                }
            }
            dropReceiveBlank.addEntry(dropReceiveRecordSingleLine);
            textViewDropBlankList.add(dropReceiveBlankSingLine);
            normalCode.add(normalCodeSingleLine);
            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }
    }

    private void inflateCodeBlocks(View dragAndDropContent){
        Context currentContext = dragAndDropContent.getContext();
//        LayoutInflater layoutInflater = LayoutInflater.from(currentContext);

        int codeBlockIndex = 0;
        LinearLayout codeBlockArea = dragAndDropContent.findViewById(R.id.question_code_block_area);
        for (String codeBlockText : codeBlocks){
            ButtonCodeBlock codeBlockButton = new ButtonCodeBlock(currentContext, codeBlockText, codeBlockIndex, this);
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR, codeBlockButton, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,10,0,10,0);
            codeBlockButtonList.add(codeBlockButton);
            codeBlockArea.addView(codeBlockButton);
            codeBlockIndex++;
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        LinearLayout questionBody = rootView.findViewById(R.id.question_body);
        @SuppressLint("InflateParams") View dragAndDropContent = layoutInflater.inflate(R.layout.question_drag_and_drop,null);
        questionBody.addView(dragAndDropContent);
        inflateCodeArea(dragAndDropContent);
        inflateCodeBlocks(dragAndDropContent);
    }


    @Override
    public void processFinish(String output) {
        Log.d("out put",output);
        updateConsole(output);
        checkAnswer(output);
    }

    @Override
    protected void checkAnswer(String output){
        String answer = "";
        if(answer.equals(output)){
            increaseGrade();
        }else{
            System.out.print("add to incorrect list");
//            QuestionActivity.addToIncorrectList(questionID);
        }
    }

    private void updateConsole(String output){
        TextView consoleTV = rootView.findViewById(R.id.console);
        output = prependArrow(output);
        consoleTV.setText(output);
    }

    @Override
    public void runAction(){
        dropReceiveBlank.print();
        for (List<TextViewDropBlank> dropBlankList: textViewDropBlankList){
            for (TextViewDropBlank textViewDropBlank : dropBlankList){
                textViewDropBlank.setClickable(false);
            }
        }
        for (Button b : codeBlockButtonList){
            b.setEnabled(false);
        }

        for (int listIndex = 0; listIndex < normalCode.size(); listIndex++){
            int index = 0;
            List<TextView> normalCodeLine = normalCode.get(listIndex);
            List<Button> dropReceiveBlanksLine = dropReceiveBlank.getBlankSpaceListSingleLine(listIndex);
            for (TextView tv : normalCodeLine){
                codeString = codeString + tv.getText();
                if (index<dropReceiveBlanksLine.size())
                    codeString = codeString + dropReceiveBlanksLine.get(index).getText();
                index++;
            }
            codeString = String.format("%s\n", codeString);
        }


        HttpPostAsyncTask request = new HttpPostAsyncTask(codeString);
        request.delegate = thisQuestionView;
        request.execute();
    }
}
