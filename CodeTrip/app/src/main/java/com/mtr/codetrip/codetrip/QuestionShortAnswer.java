package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.helper.AsyncResponse;
import com.mtr.codetrip.codetrip.helper.DropReceiveBlank;
import com.mtr.codetrip.codetrip.helper.EditTextInsert;
import com.mtr.codetrip.codetrip.helper.HttpPostAsyncTask;
import com.mtr.codetrip.codetrip.helper.LayoutUtil;
import com.mtr.codetrip.codetrip.helper.RunButton;
import com.mtr.codetrip.codetrip.helper.TextViewDropBlank;
import com.mtr.codetrip.codetrip.helper.TextViewLineNumber;
import com.mtr.codetrip.codetrip.helper.TextViewNormalCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 24/02/2018.
 */

public class QuestionShortAnswer extends Question implements AsyncResponse {
    private List<String> codeArea;
    private TextView console;
    private List<TextView> normalCode;
    protected List<EditTextInsert> editTextInsertList;
    private DropReceiveBlank receiveBlank;
    private QuestionShortAnswer thisQuestionView;
    private String codeString;
    RunButton doitButon;





    public QuestionShortAnswer(ViewGroup viewGroup){
        super(viewGroup);
        thisQuestionView = this;
        codeString = "";
        normalCode = new ArrayList<>();
        editTextInsertList = new ArrayList<EditTextInsert>();
        doitButon = rootView.findViewById(R.id.doit);
        receiveBlank = new DropReceiveBlank(context, doitButon);

    }

    @Override
    protected void populateFromDB(Cursor c){
        super.populateFromDB(c);
        codeArea =  getArrayFromDB(c, "code");
    }
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
        LinearLayout questionBody = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        View shortAnswerContent = layoutInflater.inflate(R.layout.question_short_answer,null);
        questionBody.addView(shortAnswerContent);

        console = rootView.findViewById(R.id.console);

        inflateCodeArea(shortAnswerContent);
    }


    private void inflateCodeArea(View shortAnswerContent){
        Context currentContext = shortAnswerContent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(currentContext);

        LinearLayout codeAreaLinearLayout = shortAnswerContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine = null;
        int lineIndex = 1;
        for (String codeLine : codeArea){
            String[] code = codeLine.split("(\\[\\?\\])");
            singleLine = new LinearLayout(currentContext);
            singleLine.setOrientation(LinearLayout.HORIZONTAL);
            LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR,singleLine, LayoutUtil.ParamType.MATCH_PARENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);


            TextViewLineNumber textViewLineNumber = new TextViewLineNumber(currentContext,Integer.toString(lineIndex)+".");
            LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, textViewLineNumber, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,15,0,5,0);
            singleLine.addView(textViewLineNumber);


            int index = 0, blankIndex= 0;
            for (String s :code){
                TextViewNormalCode normalTextView = new TextViewNormalCode(currentContext, s);
                LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, normalTextView, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);
                singleLine.addView(normalTextView);
                normalCode.add(normalTextView);

                if (index++ < code.length -1){
                    EditTextInsert editTextInsert = new EditTextInsert(context,this);
                    LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, editTextInsert, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,5,0,5,0);
                    singleLine.addView(editTextInsert);
                    editTextInsertList.add(editTextInsert);
                    blankIndex++;
                }
            }
            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }
    }

    public void checkEditInsertList(){
        for (EditTextInsert editTextInsert : editTextInsertList){
            String editText = editTextInsert.getText().toString();
            if (editText.equals(null) || editText.equals("")) return;
        }
        doitButon.updateDoItButtonState(RunButton.RunButtonState.RUN);
    }

    @Override
    public void processFinish(String output) {
        Log.d("out put",output);
        updateConsole(output);
    }

    private void updateConsole(String output){
        TextView consoleTV = rootView.findViewById(R.id.console);
        output = prependArrow(output);
        consoleTV.setText(output);
    }

    @Override
    public void runAction(){
//        for (List<TextViewDropBlank> dropBlankList: textViewDropBlankList){
//            for (TextViewDropBlank textViewDropBlank : dropBlankList){
//                textViewDropBlank.setClickable(false);
//            }
//        }
//        for (Button b : codeBlockButtonList){
//            b.setEnabled(false);
//        }
//
//        for (int listIndex = 0; listIndex < normalCode.size(); listIndex++){
//            int index = 0;
//            List<TextView> normalCodeLine = normalCode.get(listIndex);
//            List<Button> dropReceiveBlanksLine = dropReceiveBlank.getBlankSpaceListSingleLine(listIndex);
//            for (TextView tv : normalCodeLine){
//                codeString +=tv.getText();
//                if (index<=normalCode.size()-1) codeString+=dropReceiveBlanksLine.get(index).getText();
//                index++;
//            }
//            codeString += "\n";
//        }
//
////                        console.setText(codeString);
//
//        HttpPostAsyncTask request = new HttpPostAsyncTask(codeString);
//        request.delegate = thisQuestionView;
//        request.execute();
    }
}
