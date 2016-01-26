package com.aosijia.dragonbutler.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 发布活动
 */
public class ForumEventCreateActivity extends BaseActivity {

    private TextView tv_endDate;
    private TextView tv_startDate;
    private ImageGroupView igv_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_forum_event);
        initView();
    }

    private void initView() {
        setTitle("发起活动", null, R.drawable.btn_back, "发布", NO_RES_ID);
        TextView tv_count = (TextView) findViewById(R.id.tv_count);
        tv_endDate = (TextView) findViewById(R.id.tv_endDate);
        tv_startDate = (TextView) findViewById(R.id.tv_startDate);
        igv_pic = (ImageGroupView) findViewById(R.id.igv_pic);
        tv_startDate.setText(TimeUtils.getDateToString(String.valueOf(System.currentTimeMillis()).substring(0, 10)));
        tv_endDate.setText("长期有效");
        getEditTextContent().addTextChangedListener(new TextWatcherListener(tv_count, 2000));
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发布
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseEvent();
            }
        });
    }

    //发布活动
    private void releaseEvent() {
        String title = getEditTextTitle().getText().toString();
        String content = getEditTextContent().getText().toString();

        if (title.length() == 0) {
            Toast.makeText(this, "活动标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        if (title.length() > 20) {
            Toast.makeText(this, "活动标题最长20个字", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() == 0) {
            Toast.makeText(this, "活动内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() > 0 && content.length() < 10) {
            Toast.makeText(this, "活动内容至少10个字", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() > 2000) {
            Toast.makeText(this, "活动内容不能超过2000字", Toast.LENGTH_SHORT).show();
            return;
        }

        String startDate = TimeUtils.stringToTime(tv_startDate.getText().toString());
        String endDate = TimeUtils.stringToTime(tv_endDate.getText().toString());
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();

        Gson gson = new Gson();
        List<String> picList = igv_pic.getUploadUrl();

        String pic_url = null;

        if (picList != null && picList.size() > 0) {
            pic_url = gson.toJson(picList);
        }

        if (!igv_pic.canSubmit()) {
            Toast.makeText(this, "正在上传图片,请稍后提交", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> parameters = RequestParameters.forumTopicCreate(access_token, title, content,
                Constants.FORUM_TYPE_EVENT, pic_url, startDate, endDate, null);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_CREATE).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("正在发布...");
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumEventCreateActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ForumEventCreateActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private EditText getEditTextTitle() {
        return (EditText) findViewById(R.id.et_title);
    }

    private EditText getEditTextContent() {
        return (EditText) findViewById(R.id.et_content);
    }


    public void startDateListener(View view) {
        getDate(tv_startDate);
    }

    public void endDateListener(View view) {
        getDate(tv_endDate);
    }

    //获取时间
    public void getDate(final TextView tv) {
        final StringBuffer sb = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int minute = calendar.get(Calendar.MINUTE);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        }, year, monthOfYear, dayOfMonth);
        final DatePicker datePicker = datePickerDialog.getDatePicker();
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(ForumEventCreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sb.delete(0, sb.length());
                        sb.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                        sb.append("\u0020");
                        sb.append(String.format("%02d:%02d", hourOfDay, minute));
                        tv.setText(sb.toString());
                    }
                }, hourOfDay, minute, true);

                timePickerDialog.show();
            }
        });
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            igv_pic.onParentResult(requestCode, data);
        }
    }

}

