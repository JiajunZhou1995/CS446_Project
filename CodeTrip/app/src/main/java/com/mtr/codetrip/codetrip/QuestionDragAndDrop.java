package com.mtr.codetrip.codetrip;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        LinearLayout questionContent = rootView.findViewById(R.id.question_content);
        View dragAndDrop = layoutInflater.inflate(R.layout.question_drag_and_drop,null);
        questionContent.addView(dragAndDrop);


        View action_menu = layoutInflater.inflate(R.layout.stars_indicator,null);

        LinearLayout codeAreaLinearLayout = (LinearLayout) questionContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine = null;
        int lineIndex = 1;
        for (String codeLine : codeArea){
            String[] code = codeLine.split("(\\[\\?\\])");
            singleLine = (LinearLayout) layoutInflater.inflate(R.layout.question_code_area_single_line,null);

            TextView lineNumberTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_line_number_textview,null);
            lineNumberTextView.setText(Integer.toString(lineIndex)+".");
            singleLine.addView(lineNumberTextView);
            int index = 0;
            for (String s :code){
                TextView normalTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_normal_textview,null);
                normalTextView.setText(s);
                singleLine.addView(normalTextView);

                if (index++ < code.length -1){
                    TextView specialTextView = (TextView) layoutInflater.inflate(R.layout.question_code_area_drop_textview,null);
                    singleLine.addView(specialTextView);
                }
            }

            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }
    }

}
