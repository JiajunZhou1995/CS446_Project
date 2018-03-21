package com.mtr.codetrip.codetrip.Object;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.MainActivity;
import com.mtr.codetrip.codetrip.QuestionActivity;
import com.mtr.codetrip.codetrip.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by Catrina on 2/19/2018 at 10:18 PM.
 * Within Package ${PACKAGE_NAME}
 */

public class QuestionPageFragment extends Fragment {

//    enum
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String COURSE_ID = "courseID";


    public static final String CURRENT_QUESTION = "current_question";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int questionID;
    private int courseID;

    private Question currentQuestion;
    private QuestionActivity currentQuestionActivity;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
//    public static QuestionPageFragment create(int courseID, int pageNumber) {
//        QuestionPageFragment fragment = new QuestionPageFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, pageNumber);
//        args.putInt(COURSE_ID, courseID);
//        fragment.setArguments(args);
//        return fragment;
//    }


    public static QuestionPageFragment create() {
        QuestionPageFragment fragment = new QuestionPageFragment();
//        currentQuestion = question;
        return fragment;
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        questionID = getArguments().getInt(ARG_PAGE);
//        courseID = getArguments().getInt(COURSE_ID);
//        currentQuestion = null;
//    }

    public void setCurrentQuestion(Question question){
        currentQuestion = question;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCurrentQuestionActivity(QuestionActivity questionActivity) {
        this.currentQuestionActivity = questionActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.question_content, container, false);


        if (currentQuestion != null) {
//            currentQuestion.populateFromDB(cursor);
            currentQuestion.setRootView(rootView);
            currentQuestion.inflateContent(rootView);
            currentQuestion.setCurrentQuestionActivity(currentQuestionActivity);

            RunButton runButton = rootView.findViewById(R.id.doit);
            runButton.setCurrentQuestion(currentQuestion);
            runButton.setCurrentQuestionActivity(currentQuestionActivity);

//            currentQuestion.setUpRunButton();
            if (currentQuestion instanceof QuestionRearrange) {
                runButton.updateDoItButtonState(RunButton.RunButtonState.RUN);
            } else {
                runButton.updateDoItButtonState(RunButton.RunButtonState.INVALID);
            }
        }
        return rootView;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        String sql = "SELECT * FROM question WHERE courseid =" + Integer.toString(courseID) + " AND questionid =" + Integer.toString(questionID);
//        Cursor cursor = MainActivity.appDB.rawQuery(sql, null);
//
//        String questionType = "";
//        cursor.moveToFirst();
//        if (!cursor.isAfterLast()) {
//            questionType = cursor.getString(cursor.getColumnIndex("type"));
//        }
//
//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.question_content, container, false);
////        RunButton runButton = new RunButton(rootView.getContext());
////        rootView.addView(runButton);
//
//        // Inflate the layout containing a title and body text.
//        Question question;
//        switch (questionType) {
//            case "Rearrange":
//                question = new QuestionRearrange(rootView);
//                break;
//            case "MultipleChoice":
//                question = new QuestionMultipleChoice(rootView);
//                break;
//            case "ShortAnswer":
//                question = new QuestionShortAnswer(rootView);
//                break;
//            case "Drag&Drop":
//                question = new QuestionDragAndDrop(rootView);
//                break;
//            default:
//                question = null;
//                break;
//        }
//
//        if (question != null) {
//            question.populateFromDB(cursor);
//            question.inflateContent(rootView);
//            question.setCurrentQuestionActivity(currentQuestionActivity);
//            currentQuestion = question;
//            RunButton runButton = rootView.findViewById(R.id.doit);
//            runButton.setCurrentQuestion(currentQuestion);
//            runButton.setCurrentQuestionActivity(currentQuestionActivity);
//            if (question instanceof QuestionRearrange) {
//                runButton.updateDoItButtonState(RunButton.RunButtonState.RUN);
//            } else {
//                runButton.updateDoItButtonState(RunButton.RunButtonState.INVALID);
//            }
//        }
//        return rootView;
//    }

//    public void setBoundedQuestionActivity(QuestionActivity questionActivity){
//        runButton.setBoundedQuestionActivity(questionActivity);
//    }

    /**
     * Returns the page number represented by this fragment object.
     */
//    public int getPageNumber() {
//        return questionID;
//    }
//
    public Question getCurrentQuestion() {
        return currentQuestion;
    }
}
