package com.aosijia.dragonbutler.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class TextWatcherFactory {

    public static TextWatcher limitedLengthTextWatcher(final Context context,
                                                       final int maxlength) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > maxlength) {
                    ToastUtils.showToast(context, "最长" + maxlength + "个字符");
                    s.delete(maxlength, s.length());
                }
            }
        };

        return watcher;
    }

    public static TextWatcher limitedLengthTextWatcher(final Context context,
                                                       final int maxlength, final String message) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > maxlength) {
                    ToastUtils.showToast(context, message);
                    s.delete(maxlength, s.length());
                }
            }
        };

        return watcher;
    }
}
