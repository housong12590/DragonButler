package com.aosijia.dragonbutler.ui.widget;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextWatcherListener implements TextWatcher {

    private TextView textCount;
    private int maxLength;
    private CharSequence text;

    public TextWatcherListener(TextView textCount, int maxLength) {
        this.textCount = textCount ;
        this.maxLength = maxLength;
        textCount.setText(" 0 /" + maxLength);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        text = s;
    }

    @Override
    public void afterTextChanged(Editable s) {

       textCount.setText(text.length() + " / "+ maxLength);
        if(text.length() > maxLength){
            textCount.setTextColor(Color.RED);
        }else{
            textCount.setTextColor(Color.GRAY);
        }



    }
}
