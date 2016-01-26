package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by wanglj on 16/1/6.
 */
public class OtherUserInfoActivity extends BaseActivity implements View.OnClickListener {


    private TextView nickNameValueTextView;
    private TextView genderValueTextView;
    private ImageView avatarImageView;
    public String nickName;
    public String avatarUrl;
    public String userId;
    public String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_other_userinfo);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nickName = bundle.getString("nickName");
            avatarUrl = bundle.getString("avatar");
            userId = bundle.getString("userId");
            gender = bundle.getString("gender");
        }
        initView();
    }

    private void initView() {

        findViewById(R.id.title_leftimageview).setOnClickListener(this);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nickNameValueTextView = (TextView) findViewById(R.id.nickNameValueTextView);
        genderValueTextView = (TextView) findViewById(R.id.genderValueTextView);
        findViewById(R.id.sendMessageButton).setOnClickListener(this);
        setNickNameText();
        setAvatarImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview://返回
                finish();
                break;
            case R.id.sendMessageButton:
                Intent intent = new Intent(this, MessagesActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("userId", userId);
                intent.putExtra("avatar", avatarUrl);
                intent.putExtra("nickName", nickName);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }


    private void setGenderText() {
        if ("0".equals(gender)) {//女
            genderValueTextView.setText("女");
        } else if ("1".equals(gender)) {//男
            genderValueTextView.setText("男");
        } else {//2 保密
            genderValueTextView.setText("保密");
        }
    }

    private void setAvatarImage() {
        if (!TextUtils.isEmpty(avatarUrl)) {
            ImageLoader.getInstance().displayImage(avatarUrl, avatarImageView, DisplayOpitionFactory.sAvatarDisplayOption);
        }
    }

    private void setNickNameText() {
        nickNameValueTextView.setText(nickName);
    }


}
