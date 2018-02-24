package com.mtr.codetrip.codetrip;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Catrina on 2/19/2018.
 */

public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = null;
        // Inflate the layout containing a title and body text.

        if (mPageNumber==0) rootView = (ViewGroup) inflater.inflate(R.layout.layout_question, container, false);
        else rootView = (ViewGroup) inflater.inflate(R.layout.content_favorite, container, false);
        // Set the title view to show the page number.
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                getString(R.string.title_template_step, mPageNumber + 1));

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
