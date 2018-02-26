package com.mtr.codetrip.codetrip;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Catrina on 24/02/2018.
 */

public class QuestionShortAnswer extends Question {
    private List<String> codeArea;


    public QuestionShortAnswer(ViewGroup viewGroup){
        super(viewGroup);
    }

    @Override
    public void populateFromDB(Cursor c){
        super.populateFromDB(c);

        codeArea =  getArrayFromDB(c, "code");
    }
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
        LinearLayout questionContent = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        View dragAndDrop = layoutInflater.inflate(R.layout.question_short_answer,null);
        questionContent.addView(dragAndDrop);
    }
}
