package com.e16din.goeurotest.view;

import android.content.Context;
import android.util.AttributeSet;

public class AutoCompleteTextView extends android.widget.AutoCompleteTextView {

    public AutoCompleteTextView(Context context) {
        this(context, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.autoCompleteTextViewStyle);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }
}
