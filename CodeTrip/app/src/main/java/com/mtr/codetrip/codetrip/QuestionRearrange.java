package com.mtr.codetrip.codetrip;

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

import com.mtr.codetrip.codetrip.helper.OnStartDragListener;
import com.mtr.codetrip.codetrip.helper.RecyclerListAdapter;
import com.mtr.codetrip.codetrip.helper.SimpleItemTouchHelperCallback;

import java.util.List;

/**
 * Created by j66zhu on 2018-02-24.
 */

public class QuestionRearrange extends Question implements OnStartDragListener{
    private ItemTouchHelper mItemTouchHelper;
    private List<String> codeIns;

    QuestionRearrange(ViewGroup view){
        super(view);

    }

    @Override
    public void populateFromDB(Cursor cursor){
        super.populateFromDB(cursor);

        codeIns = getArrayFromDB(cursor, "code");

    }

    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);

        LinearLayout questionContent = rootView.findViewById(R.id.question_content);
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

        Button doit = rootView.findViewById(R.id.doit);
        doit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RUN_BUTTON_STATUS status = updateStatus(adapter);
            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private RUN_BUTTON_STATUS updateStatus(RecyclerListAdapter adapter){
        String currentOrder = adapter.getItems().toString();
        currentOrder = currentOrder.replaceAll(", ", "\n");
        currentOrder = currentOrder.substring(1, currentOrder.length()-1);
        Log.d("jasmine", currentOrder);
        return RUN_BUTTON_STATUS.RUN;
    }

//    private String resultFromServer(String msg){

//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://www.google.com";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        //mTextView.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //mTextView.setText("That didn't work!");
//            }
//        });
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
}
