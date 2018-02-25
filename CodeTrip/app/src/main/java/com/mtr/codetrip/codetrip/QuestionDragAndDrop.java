package com.mtr.codetrip.codetrip;

import android.database.Cursor;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.List;

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

        try{
            codeArea =  getArrayFromDB(c, "code");
            codeBlocks = getArrayFromDB(c, "codeblocks");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
