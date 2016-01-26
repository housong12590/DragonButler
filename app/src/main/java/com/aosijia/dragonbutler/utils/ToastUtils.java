package com.aosijia.dragonbutler.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
    public static void showToast(Context context, String content) {
        if (context == null)
            return;
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
    }

//    public static void showCustomToast(Context context, String content) {
//        View layout = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
//        TextView tv = (TextView) layout.findViewById(R.id.textView);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        params.width = width;
//        tv.setLayoutParams(params);
//        tv.setText(content);
//        Toast toast = new Toast(context);
//        toast.setView(layout);
//        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, context.getResources().getDimensionPixelSize(R.dimen.title_bar_height));
//        toast.show();
//    }
}
