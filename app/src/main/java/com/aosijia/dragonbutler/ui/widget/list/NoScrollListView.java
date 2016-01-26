package com.aosijia.dragonbutler.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 此ListView禁用ListView自带滚动条，作用：此控件在与其他控件放在一起滚动
 * 
 * @author wanglj 2014-6-12
 */
public class NoScrollListView extends ListView {

	public NoScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
