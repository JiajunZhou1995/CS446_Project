package com.mtr.codetrip.codetrip.Utility;

import android.database.Cursor;

import com.mtr.codetrip.codetrip.MainActivity;

import java.util.List;
import java.util.Map;

import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getArrayFromDB;

/**
 * Created by catrina on 2018-03-17.
 */

public class QuestionPicker {
    private Map<String,List<List<Integer>>> questionMap;
    private List<String> topicList;
    private List<Integer> questionList;
    int hey = -1;

    public void generateQuestionMap(int courseID){
        String sql = "SELECT * FROM course WHERE courseid =" + Integer.toString(courseID);
        Cursor cursor = MainActivity.myDB.rawQuery(sql, null);
        cursor.moveToFirst();

        topicList = getArrayFromDB(cursor, "topics");
    }


}
