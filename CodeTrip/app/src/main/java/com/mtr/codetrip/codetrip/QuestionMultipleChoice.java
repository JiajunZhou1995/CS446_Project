package com.mtr.codetrip.codetrip;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.helper.LayoutUtil;
import com.mtr.codetrip.codetrip.helper.TextViewLineNumber;
import com.mtr.codetrip.codetrip.helper.TextViewNormalCode;

import com.mtr.codetrip.codetrip.helper.AsyncResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 24/02/2018.
 */

public class QuestionMultipleChoice extends Question implements AsyncResponse{

    private List<String> codeIns;
    private List<String> choices;
    private int currentSelection;
    private List<FrameLayout> choiceViews;


    public QuestionMultipleChoice(ViewGroup viewGroup){
        super(viewGroup);
        codeIns = new ArrayList<String>();
        choices = new ArrayList<String>();
        choiceViews = new ArrayList<FrameLayout>();
    }

    @Override
    protected void populateFromDB(Cursor c){
        super.populateFromDB(c);
        codeIns =  getArrayFromDB(c, "code");
        choices = getArrayFromDB(c, "choice");
    }


    private void updateSelection(int newSelection){
        int lastSelection = currentSelection;
        currentSelection = newSelection;
        if (lastSelection != currentSelection){
            choiceViews.get(lastSelection).setBackground(context.getDrawable(R.drawable.code_area_round));
        }
    }

    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);

        LinearLayout questionContent = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        View multiple_choice = layoutInflater.inflate(R.layout.question_multiple_choice,null);
        questionContent.addView(multiple_choice);

        LinearLayout codeAreaLinearLayout = (LinearLayout) questionContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine;
        int lineIndex = 1;
        if (codeIns.size() == 0){
            View code_area = rootView.findViewById(R.id.question_code_area);
            code_area.setVisibility(View.GONE);
        }
        for (String codeLine : codeIns){
            singleLine = new LinearLayout(context);
            singleLine.setOrientation(LinearLayout.HORIZONTAL);
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR,singleLine, LayoutUtil.ParamType.MATCH_PARENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);


            TextViewLineNumber textViewLineNumber = new TextViewLineNumber(context,Integer.toString(lineIndex)+".");
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR,textViewLineNumber, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,15,0,5,0);
            singleLine.addView(textViewLineNumber);

            TextViewNormalCode normalTextView = new TextViewNormalCode(context, codeLine);
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR, normalTextView, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);
            singleLine.addView(normalTextView);

            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }
        for (String choice: choices){
            LinearLayout choiceArea = rootView.findViewById(R.id.question_choice_area);
            FrameLayout choiceView = (FrameLayout) layoutInflater.inflate(R.layout.mc_item_layout, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,10,5,10);
            choiceView.setLayoutParams(params);
            TextView choiceTV = choiceView.findViewById(R.id.mc_item_text);
            choiceTV.setText(choice);

            final int choiceIndex = choices.indexOf(choice);
            choiceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelection(choiceIndex);
                    v.setBackground(context.getDrawable(R.drawable.code_area_round_highlight));
                }
            });
            choiceViews.add(choiceView);
            choiceArea.addView(choiceView);
        }
    }

    public void processFinish(String output){

    }
}
