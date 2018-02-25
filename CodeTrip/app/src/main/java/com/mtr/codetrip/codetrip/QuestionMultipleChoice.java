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

public class QuestionMultipleChoice extends Question {

    private List<String> codeArea;
    private List<String> codeBlocks;


    public QuestionMultipleChoice(ViewGroup viewGroup){
        super(viewGroup);
    }

    @Override
    public void populateFromDB(Cursor c){
        super.populateFromDB(c);

        codeArea =  getArrayFromDB(c, "code");
        codeBlocks = getArrayFromDB(c, "codeblock");
    }
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
//        LinearLayout questionContent = rootView.findViewById(R.id.question_content);
//        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
//        View dragAndDrop = layoutInflater.inflate(R.layout.question_multiple_choice,null);
//        questionContent.addView(dragAndDrop);
    }
}
