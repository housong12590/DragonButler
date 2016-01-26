package com.aosijia.dragonbutler.ui.widget;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;

/**
 * Created by wanglj on 15/12/31.
 */
public class LoadingDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.LoadingDialog);
//		return new LoadingDialog(getActivity(), R.style.LoadingDialog, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_loading, container, false);
        iv = (ImageView) view.findViewById(R.id.imageview);
        TextView message = (TextView) view.findViewById(R.id.message);
//        message.setText(getTag());
        AnimationDrawable ad = (AnimationDrawable) iv.getDrawable();
        ad.start();
        return view;
    }
    ImageView iv;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AnimationDrawable ad = (AnimationDrawable) iv.getDrawable();
        ad.start();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        AnimationDrawable ad = (AnimationDrawable) iv.getDrawable();
        ad.stop();
    }
}
