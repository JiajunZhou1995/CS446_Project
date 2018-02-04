package com.mtr.codetrip.codetrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Catrina on 2/4/2018.
 */

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        String file_name;
        if(savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if(extras==null){
                file_name = null;
            }else{
                file_name = extras.getString("question_file_name");
            }
        }else{
            file_name = (String)savedInstanceState.getSerializable("question_file_name");
        }
    }
}
