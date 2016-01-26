package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.aosijia.dragonbutler.utils.TextWatcherFactory;
import com.aosijia.dragonbutler.utils.ToastUtils;

/**
 * 发起投票-设置标题和内容
 * Created by Jacky on 2015/12/29.
 * Version 1.0
 */
public class ForumVoteCreateActivity extends BaseActivity {
    private static final int REQUEST_CODE_NEXT = 1002;

    private EditText etTitle;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_forum_vote_create);
        initView();
    }

    private void initView() {
        setTitle("发起投票", null, R.drawable.btn_back, "下一步", NO_RES_ID);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ForumVoteCreateOptionsActivity.class);
                String title = getTextNoSpace(etTitle);
                String content = getTextNoSpace(etContent);
                if (TextUtils.isEmpty(title)) {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.error_title_empty));
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(getApplicationContext(), getString(R.string.error_desc_empty));
                    return;
                }
                intent.putExtra(ForumVoteCreateOptionsActivity.EXTRA_TITLE, title);
                intent.putExtra(ForumVoteCreateOptionsActivity.EXTRA_CONTENT, content);
                startActivityForResult(intent, REQUEST_CODE_NEXT);
            }
        });
        etTitle = (EditText) findViewById(R.id.et_title);
        etTitle.addTextChangedListener(TextWatcherFactory.limitedLengthTextWatcher(this, 20, "标题最长20个字"));
        etContent = (EditText) findViewById(R.id.et_content);
        etContent.addTextChangedListener(TextWatcherFactory.limitedLengthTextWatcher(this, 200, "描述最长200个字"));
        TextView textCount = (TextView) findViewById(R.id.textCount);
        etContent.addTextChangedListener(new TextWatcherListener(textCount,200));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEXT) {
            if(resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                this.finish();
            }
        }
    }
}
