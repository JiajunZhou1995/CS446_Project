package com.mtr.codetrip.codetrip.CostumWidgets;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.*;
import com.mtr.codetrip.codetrip.Object.Question;
import com.mtr.codetrip.codetrip.Object.QuestionShortAnswer;
import com.mtr.codetrip.codetrip.Utility.DensityUtil;

/**
 * Created by Catrina on 2/26/2018.
 */

public class EditTextInsert extends android.support.v7.widget.AppCompatEditText implements TextWatcher, TextView.OnEditorActionListener {


    private Context context;
    private Question question;

    public EditTextInsert(Context context, Question question){
        super(context);
        this.context = context;
        this.question = question;
        this.setPadding(DensityUtil.dip2px(context,20), DensityUtil.dip2px(context,5), DensityUtil.dip2px(context,20), DensityUtil.dip2px(context,5));
        this.setBackground(context.getDrawable(R.drawable.drop_block_round_enabled));
        this.setTextSize(DensityUtil.dip2px(context,14));
        this.setTextAppearance(R.style.FontStyle_DropBlank);
        this.setHint("?");
        this.setSelectAllOnFocus(true);
        this.setInputType(InputType.TYPE_CLASS_TEXT);
        this.setSingleLine(true);
        this.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.setCursorVisible(false);


        InputFilter[] filters = {new InputFilter.LengthFilter(20)};
        this.setFilters(filters);

        this.setOnEditorActionListener(this);
        this.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
            ((QuestionShortAnswer)question).checkEditInsertList();
        }
        return  true;
    }
}
