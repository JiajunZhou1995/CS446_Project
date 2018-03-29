package com.mtr.codetrip.codetrip.Utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.graphics.Color;

import com.mtr.codetrip.codetrip.R;


/**
 * Created by Yolo on 2018-02-24 at 12:17 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class ExpandAdapter extends BaseExpandableListAdapter {
    //    private static final String TAG = "NormalExpandableListAda";
    private String[] groupData;
    private String[][] childData;
    private int[] colorArray;
    private onExpandListener mOnGroupExpandedListener;
    private int colorindex = 0;


    public ExpandAdapter(String[] groupData, String[][] childData) {
        this.groupData = groupData;
        this.childData = childData;
        colorArray = new int[6];
        colorArray[0] = R.color.colorTiff;
        colorArray[1] = R.color.colorGrass;
        colorArray[2] = R.color.colorPineapple;
        colorArray[3] = R.color.colorChannel;
        colorArray[4] = R.color.colorPurple;
        colorArray[5] = R.color.colorOcean;

    }


    public void setOnGroupExpandedListener(onExpandListener onGroupExpandedListener) {
        mOnGroupExpandedListener = onGroupExpandedListener;
    }


    @Override
    public int getGroupCount() {
        return groupData.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View
            convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandlistview, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.label_group_normal);
            convertView.setTag(groupViewHolder);
            convertView.setBackgroundResource(R.color.colorWhite);
            colorindex++;
            colorindex = colorindex % 6;
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupData[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.childexpand, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.label_expand_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }


        childViewHolder.tvTitle.setText(childData[groupPosition][childPosition]);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        if (mOnGroupExpandedListener != null) {
            mOnGroupExpandedListener.setOnExpandListener(groupPosition);
        }
    }


    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    private static class GroupViewHolder {
        TextView tvTitle;
    }

    private static class ChildViewHolder {
        TextView tvTitle;
    }
}

