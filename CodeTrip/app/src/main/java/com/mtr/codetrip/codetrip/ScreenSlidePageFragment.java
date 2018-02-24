package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Catrina on 2/19/2018.
 */

public class ScreenSlidePageFragment extends Fragment {

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
    public static ScreenSlidePageFragment create(int courseID, int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
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

//        SQLiteDatabase myDB = getContext().openOrCreateDatabase("codetrip.db", Context.MODE_PRIVATE,null);
        String sql = "SELECT * FROM question WHERE courseid =" + Integer.toString(courseID) +" AND questionid =" + Integer.toString(questionID);
//        String sql = "SELECT * FROM question WHERE courseid =" + "1" +" AND questionid =" + "1";
        Cursor cursor =  MainActivity.myDB.rawQuery(sql, null);

        String questionType = "";
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            questionType = cursor.getString(cursor.getColumnIndex("type"));
        }
//        while(!c.isAfterLast()){
//            int index = c.getColumnIndex("courseid");
////            Log.d("!!!!!!!!!!SQLite", c.getString(index));
//            Log.d("current type", questionType);
//            c.moveToNext();
//        }
//        String questionType = cursor.getString(cursor.getColumnIndex("type"));
//        Log.d("current type", questionType);
        //String questionType = "Rearrange";

        ViewGroup rootView;
        // Inflate the layout containing a title and body text.

        switch (questionType){
            case "Rearrange":
                rootView = (ViewGroup) inflater.inflate(R.layout.question_rearrange,container,false);
                break;
            case "MultipleChoice":
                rootView = (ViewGroup) inflater.inflate(R.layout.question_multiple_choice,container,false);
                break;
            case "ShortAnswer":
                rootView = (ViewGroup) inflater.inflate(R.layout.question_short_answer,container,false);
                break;
            case "Drag&Drop":
                rootView = (ViewGroup) inflater.inflate(R.layout.question_drag_and_drop,container,false);
                break;
            default:
                rootView = null;
                break;
        }

//        if (questionID==0) rootView = (ViewGroup) inflater.inflate(R.layout.layout_question, container, false);
//        else rootView = (ViewGroup) inflater.inflate(R.layout.content_favorite, container, false);
        // Set the title view to show the page number.
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                getString(R.string.title_template_step, mPageNumber + 1));

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return questionID;
    }
}
