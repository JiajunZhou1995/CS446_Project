package com.mtr.codetrip.codetrip.Object;

import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.AsyncResponse;
import com.mtr.codetrip.codetrip.Utility.HttpPostAsyncTask;
import com.mtr.codetrip.codetrip.Utility.OnStartDragListener;
import com.mtr.codetrip.codetrip.Utility.RecyclerListAdapter;
import com.mtr.codetrip.codetrip.Utility.SimpleItemTouchHelperCallback;

import java.util.List;

/**
 * Created by j66zhu on 2018-02-24.
 */

public class QuestionRearrange extends Question implements OnStartDragListener, AsyncResponse{
    private ItemTouchHelper mItemTouchHelper;
    private List<String> codeIns;
    private String answer;

    QuestionRearrange(ViewGroup view){
        super(view);
    }

    @Override
    protected void populateFromDB(Cursor c){
        super.populateFromDB(c);

        codeIns = getArrayFromDB(c, "code");
        answer = c.getString(c.getColumnIndex("answer"));
    }

    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);

        LinearLayout questionContent = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        View rearrange = layoutInflater.inflate(R.layout.question_rearrange,null);
        questionContent.addView(rearrange);

        final RecyclerListAdapter adapter = new RecyclerListAdapter(rootView.getContext(), this, codeIns);

        final RecyclerView recyclerView = rootView.findViewById(R.id.rearrange_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        Button doIt = rootView.findViewById(R.id.doit);
        doIt.setClickable(true);
        doIt.setBackground(context.getDrawable(R.drawable.doit_button_run));
        doIt.setText(context.getString(R.string.question_action_run));
        doIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RUN_BUTTON_STATUS status = updateAnswer(adapter);
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private RUN_BUTTON_STATUS updateAnswer(RecyclerListAdapter adapter){
        String currentOrder = adapter.getItems().toString();
        currentOrder = currentOrder.replaceAll(", ", "\n");
        currentOrder = currentOrder.substring(1, currentOrder.length()-1);
        Log.d("jasmine", currentOrder);

        HttpPostAsyncTask asyncTask = new HttpPostAsyncTask(currentOrder);
        asyncTask.delegate = this;
        asyncTask.execute();

        return RUN_BUTTON_STATUS.RUN;
    }

    public void processFinish(String output){
        if (output.equals(answer)){
            //Log.d("jasmine", "correct!");
        }

        status = RUN_BUTTON_STATUS.CONTINUE;
        updateButton();
        updateConsole(output);
    }

    private void updateButton(){
        Button doIt = rootView.findViewById(R.id.doit);
        if (status == RUN_BUTTON_STATUS.CONTINUE){
        }
        else {
        }

        doIt.setClickable(true);
        doIt.setBackground(context.getDrawable(R.drawable.doit_button_continue));
        doIt.setText(context.getString(R.string.question_action_continue));
    }

    private void updateConsole(String output){
        TextView consoleTV = rootView.findViewById(R.id.console);
        output = prependArrow(output);
        consoleTV.setText(output);
    }

}
