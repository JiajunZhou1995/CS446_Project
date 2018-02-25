package com.mtr.codetrip.codetrip;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mtr.codetrip.codetrip.helper.OnStartDragListener;
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

        RecyclerListAdapter adapter = new RecyclerListAdapter(rootView.getContext(), this, codeIns);

        RecyclerView recyclerView = rootView.findViewById(R.id.rearrange_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
