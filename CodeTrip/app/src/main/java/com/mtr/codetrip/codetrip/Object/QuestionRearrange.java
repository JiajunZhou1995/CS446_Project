package com.mtr.codetrip.codetrip.Object;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.QuestionActivity;
import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.AsyncResponse;
import com.mtr.codetrip.codetrip.Utility.HttpPostAsyncTask;
import com.mtr.codetrip.codetrip.Utility.OnStartDragListener;
import com.mtr.codetrip.codetrip.Utility.RecyclerListAdapter;
import com.mtr.codetrip.codetrip.Utility.SimpleItemTouchHelperCallback;

import java.util.List;

import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getStrArrayFromDB;

/**
 * Created by j66zhu on 2018-02-24 at 11:58 PM.
 * Within Package: ${PACKAGE_NAME}
 */

public class QuestionRearrange extends Question implements OnStartDragListener, AsyncResponse {
    private ItemTouchHelper mItemTouchHelper;
    private List<String> codeIns;
    private String answer;
//    private RunButton doIt;
    private RecyclerListAdapter adapter;


    QuestionRearrange(ViewGroup view) {
        super(view);
    }

    public QuestionRearrange(){super();}

    @Override
    public void populateFromDB(Cursor c) {
        super.populateFromDB(c);

        codeIns = getStrArrayFromDB(c, "code");
        answer = c.getString(c.getColumnIndex("answer"));
//        doIt = rootView.findViewById(R.id.doit);
    }

    @Override
    public void setRootView(ViewGroup viewGroup){
        super.setRootView(viewGroup);
//        doIt = rootView.findViewById(R.id.doit);
    }

    @Override
    protected void inflateContent(ViewGroup rootView) {
        super.inflateContent(rootView);

        LinearLayout questionContent = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        @SuppressLint("InflateParams") View rearrange = layoutInflater.inflate(R.layout.question_rearrange, null);
        questionContent.addView(rearrange);

        adapter = new RecyclerListAdapter(rootView.getContext(), this, codeIns);

        RecyclerView recyclerView = rootView.findViewById(R.id.rearrange_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        //Button doIt = rootView.findViewById(R.id.doit);
//        doIt.setClickable(true);
//        doIt.setBackground(context.getDrawable(R.drawable.run_button_run));
//        doIt.setText(context.getString(R.string.question_action_run));
//        doIt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RUN_BUTTON_STATUS status = updateAnswer(adapter);
//            }
//        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void updateAnswer() {
        String currentOrder = adapter.getItems().toString();
        currentOrder = currentOrder.replaceAll(", ", "\n");
        currentOrder = currentOrder.substring(1, currentOrder.length() - 1);
        Log.d("jasmine", currentOrder);

        HttpPostAsyncTask asyncTask = new HttpPostAsyncTask(currentOrder);
        asyncTask.delegate = this;
        asyncTask.execute();

        //return RUN_BUTTON_STATUS.RUN;
    }
    @Override
    protected void checkAnswer(String output){
        if(output.equals(answer)){
            Log.d("correct!!","increase score");
            increaseGrade();
            notifyObservers(true);
//            currentQuestionActivity.questionPicker.ganerateNextQuestion(true);
            currentQuestionActivity.generateNextQuestion(true);

        }else{
            Log.d("incorrect answer","add to incorrect list");
            QuestionActivity.incorrectQuestionList.add(questionID);
//            QuestionActivity.addToIncorrectList(questionID);
            notifyObservers(false);
//            currentQuestionActivity.questionPicker.ganerateNextQuestion(false);
            currentQuestionActivity.generateNextQuestion(false);

        }
//        Question newQuestion = currentQuestionActivity.questionPicker.getCurrentQuestion();
//        if (newQuestion!=null){
//            currentQuestionActivity.NUM_PAGES++;
//            currentQuestionActivity.notifyChange();
//        }
    }



    public void processFinish(String output) {
//        if (output.equals(answer)) {
//            //Log.d("jasmine", "correct!");
//            doIt.updateDoItButtonState(RunButton.RunButtonState.CONTINUE);
//        }
        checkAnswer(output);

        updateConsole(output);
        runButton.updateDoItButtonState(RunButton.RunButtonState.CONTINUE);
    }

    private void updateConsole(String output) {
        TextView consoleTV = rootView.findViewById(R.id.console);
        output = prependArrow(output);
        consoleTV.setText(output);
    }

    @Override
    public void runAction() {
        super.runAction();
        updateAnswer();
    }
}
