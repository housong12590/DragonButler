package com.aosijia.dragonbutler.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.Complaints;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.GalleryAdapter;
import com.aosijia.dragonbutler.ui.widget.CommonDialog;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglj on 15/12/25.
 */
public class ComplaintsDetailActivity extends BaseActivity implements GalleryAdapter.OnItemClickLitener {

    private TextView detailTitleTextView;

    private RecyclerView recyclerView;
    private TextView detailDateTextView;
    private TextView detailStatusTextView;
    private Button confirmOrCancelButton;
    private View divider;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_detail);
        setTitle("详情", null, R.drawable.btn_back, null, NO_RES_ID);

        final Complaints.DataEntity.ComplaintsEntity complaintsEntity =
                (Complaints.DataEntity.ComplaintsEntity) getIntent().getExtras().get("complaintsEntity");

        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.dateTextView)).setText("投诉时间");
        ((TextView) findViewById(R.id.statusTextView)).setText("投诉状态");
        divider = findViewById(R.id.divider);
        detailTitleTextView = (TextView) findViewById(R.id.detailTitleTextView);
        recyclerView = (RecyclerView) findViewById(R.id.rv_gallery);
        detailDateTextView = (TextView) findViewById(R.id.detailDateTextView);
        detailStatusTextView = (TextView) findViewById(R.id.detailStatusTextView);
        confirmOrCancelButton = (Button) findViewById(R.id.bt_confirmOrCancel);

        if (complaintsEntity.getPic_urls().size() == 0) {
            divider.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            list = complaintsEntity.getPic_urls();
            GalleryAdapter mAdapter = new GalleryAdapter(this, list);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickLitener(this);
        }

        confirmOrCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = Integer.parseInt(v.getTag().toString());
                if (status == 1) {
                    new CommonDialog.Builder(ComplaintsDetailActivity.this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定取消投诉？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelComplaint(complaintsEntity.getComplaint_id());
                                }
                            }).show();
                } else if (status == 2) {
                    new CommonDialog.Builder(ComplaintsDetailActivity.this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定投诉已完成？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    confirmComplaint(complaintsEntity.getComplaint_id());
                                }
                            }).show();
                }
            }
        });


//        imageGroupView = (ImageGroupView) findViewById(R.id.imageGroupView);
//        imageGroupView.setFragmentManager(getSupportFragmentManager());
//        imageGroupView.setNetworkPhotos(createTestData());
//        imageGroupView.setNetworkPhotos(complaintsEntity.getPic_urls());

        String statusText = "待处理";
        confirmOrCancelButton.setVisibility(View.VISIBLE);
        if ("1".equals(complaintsEntity.getStatus())) {
            statusText = "待处理";
            confirmOrCancelButton.setText("取消投诉");
            confirmOrCancelButton.setTag(1);
        } else if ("2".equals(complaintsEntity.getStatus())) {
            statusText = "处理中";
            confirmOrCancelButton.setText("确认完成");
            confirmOrCancelButton.setTag(2);
        } else if ("3".equals(complaintsEntity.getStatus())) {
            statusText = "已处理";
            confirmOrCancelButton.setText("确认完成");
            confirmOrCancelButton.setTag(2);
        } else if ("4".equals(complaintsEntity.getStatus())) {
            statusText = "已完成";
            confirmOrCancelButton.setVisibility(View.GONE);
        }
        detailStatusTextView.setText(statusText);
        detailTitleTextView.setText(complaintsEntity.getContent());
        detailDateTextView.setText(TimeUtils.getDateToString(complaintsEntity.getCreated_at()));
    }


    /**
     * 取消投诉
     *
     * @param complaintId
     */
    private void cancelComplaint(String complaintId) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.complaintCancel(complaintId, loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.COMPLAINT_CANCEL).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在取消...");
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ComplaintsDetailActivity.this)) {
                    Toast.makeText(ComplaintsDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ComplaintsDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 确认投诉
     *
     * @param complaintId
     */
    private void confirmComplaint(String complaintId) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.complaintConfirm(complaintId, loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.COMPLAINT_CONFIRM).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                dismissProgressDialog();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在确认...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ComplaintsDetailActivity.this)) {
                    Toast.makeText(ComplaintsDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ComplaintsDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        ArrayList<SquareImage> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            results.add(new SquareImage("", list.get(i), "", "", SquareImage.PhotoType.NETWORK));
        }
        NavigatorImage.startImageSwitcherActivity(ComplaintsDetailActivity.this, results, position, false, R.drawable.default_pic_item);
    }
}
