package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by Catrina on 2/19/2018.
 */

public class QuestionPageFragment extends Fragment {

//    enum
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String COURSE_ID = "courseID";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int questionID;
    private int courseID;

    public List<Question> questionList;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static QuestionPageFragment create(int courseID, int pageNumber) {
        QuestionPageFragment fragment = new QuestionPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putInt(COURSE_ID,courseID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionID = getArguments().getInt(ARG_PAGE);
        courseID = getArguments().getInt(COURSE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String sql = "SELECT * FROM question WHERE courseid =" + Integer.toString(courseID) +" AND questionid =" + Integer.toString(questionID);
        Cursor cursor = MainActivity.myDB.rawQuery(sql, null);

        String questionType = "";
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            questionType = cursor.getString(cursor.getColumnIndex("type"));
        }


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.question_content,container,false);
        // Inflate the layout containing a title and body text.

        switch (questionType){
            case "Rearrange":
//                rootView = (ViewGroup) inflater.inflate(R.layout.question_content,container,false);
                QuestionRearrange questionRearrange = new QuestionRearrange(rootView);
                questionRearrange.populateFromDB(cursor);
                questionRearrange.inflateContent(rootView);
                break;
            case "MultipleChoice":
//                rootView = (ViewGroup) inflater.inflate(R.layout.question_content,container,false);
                QuestionMultipleChoice questionMultipleChoice = new QuestionMultipleChoice(rootView);
                questionMultipleChoice.populateFromDB(cursor);
                questionMultipleChoice.inflateContent(rootView);
                break;
            case "ShortAnswer":
//                rootView = (ViewGroup) inflater.inflate(R.layout.question_content,container,false);
                QuestionShortAnswer questionShortAnswer = new QuestionShortAnswer(rootView);
                questionShortAnswer.populateFromDB(cursor);
                questionShortAnswer.inflateContent(rootView);
                break;
            case "Drag&Drop":
//                rootView = (ViewGroup) inflater.inflate(R.layout.question_content,container,false);
                QuestionDragAndDrop questionDragAndDrop = new QuestionDragAndDrop(rootView);
                questionDragAndDrop.populateFromDB(cursor);
                questionDragAndDrop.inflateContent(rootView);
                break;
            default:
                rootView = null;
                break;
        }
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return questionID;
    }
}
