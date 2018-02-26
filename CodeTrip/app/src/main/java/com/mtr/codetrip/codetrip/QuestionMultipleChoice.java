package com.mtr.codetrip.codetrip;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 24/02/2018.
 */

public class QuestionMultipleChoice extends Question{

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
    public void populateFromDB(Cursor c){
        super.populateFromDB(c);
        codeIns =  getArrayFromDB(c, "code");
        choices = getArrayFromDB(c, "choice");
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

        LinearLayout questionContent = rootView.findViewById(R.id.question_content);
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
            singleLine = (LinearLayout) layoutInflater.inflate(R.layout.question_code_area_single_line,null);

            TextView lineNumberTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_line_number_textview,null);
            setUpView(lineNumberTextView,1,1,15,0,5,0);
            lineNumberTextView.setText(String.format("%d.",lineIndex++));
            singleLine.addView(lineNumberTextView);

            TextView normalTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_normal_textview,null);
            setUpView(normalTextView,1,1,0,0,0,0);
            normalTextView.setText(codeLine);
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
}
