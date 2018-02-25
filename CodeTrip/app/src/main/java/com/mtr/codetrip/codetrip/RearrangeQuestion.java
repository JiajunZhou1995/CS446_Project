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

import com.mtr.codetrip.codetrip.helper.OnStartDragListener;
import com.mtr.codetrip.codetrip.helper.SimpleItemTouchHelperCallback;

/**
 * Created by j66zhu on 2018-02-24.
 */

public class RearrangeQuestion extends Question implements OnStartDragListener{
    private ItemTouchHelper mItemTouchHelper;

    RearrangeQuestion(View view){
        super(view);

    }
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


//    public class RearrangeFragment {
//        private ItemTouchHelper mItemTouchHelper;
//
//        public RearrangeFragment() {
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            return new RecyclerView(container.getContext());
//        }
//
//        @Override
//        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);
//
//            RecyclerListAdapter adapter = new RecyclerListAdapter(getActivity(), this);
//
//            RecyclerView recyclerView = (RecyclerView) view;
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//            mItemTouchHelper = new ItemTouchHelper(callback);
//            mItemTouchHelper.attachToRecyclerView(recyclerView);
//        }
//
//
//    }

}
