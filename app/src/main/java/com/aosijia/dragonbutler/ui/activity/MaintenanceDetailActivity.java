package com.aosijia.dragonbutler.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.MaintenanceOrders;
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
 * 报修详情
 */
public class MaintenanceDetailActivity extends BaseActivity implements View.OnClickListener,
        GalleryAdapter.OnItemClickLitener {

    private int status;
    private View divider;
    private TextView tv_date;
    private TextView tv_status;
    private TextView tv_content;
    private RecyclerView rv_pic;
    private List<String> picUrls;
    private Button bt_confirmOrCancel;
    private static final int STATUS_UNTREATED = 1;//待处理
    private static final int STATUS_UNDERWAY = 2;//处理中
    private static final int STATUS_ACCOMPLISH = 3;//已处理
    private static final int STATUS_CONFIRM = 4; //待确认
    private MaintenanceOrders.DataEntity.MaintenaceOrdersEntity mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_detail);
        mData = (MaintenanceOrders.DataEntity.MaintenaceOrdersEntity) getIntent()
                .getExtras().get(MaintenanceActivity.INTENT_FLAG);
        initView();
        setData();

    }

    private void initView() {
        setTitle(getResources().getString(R.string.detail), null, R.drawable.btn_back, null, NO_RES_ID);
        ((TextView) findViewById(R.id.dateTextView)).setText("报修时间");
        ((TextView) findViewById(R.id.statusTextView)).setText("报修状态");
        divider = findViewById(R.id.divider);
        rv_pic = (RecyclerView) findViewById(R.id.rv_gallery);
        tv_date = (TextView) findViewById(R.id.detailDateTextView);
        tv_content = (TextView) findViewById(R.id.detailTitleTextView);
        tv_status = (TextView) findViewById(R.id.detailStatusTextView);
        bt_confirmOrCancel = (Button) findViewById(R.id.bt_confirmOrCancel);
        rv_pic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bt_confirmOrCancel.setOnClickListener(this);
        setBtnLeftOnClickListener(this);
    }

    private void setData() {
        tv_content.setText(mData.getContent());
        tv_date.setText(TimeUtils.getDateToString(mData.getCreated_at()));
        status = Integer.parseInt(mData.getStatus());
        String statusText = "待处理";
        bt_confirmOrCancel.setVisibility(View.VISIBLE);
        if (STATUS_UNTREATED == status) {
            statusText = "待处理";
            bt_confirmOrCancel.setText("取消报修");
        } else if (STATUS_UNDERWAY == status) {
            statusText = "处理中";
            bt_confirmOrCancel.setText("确认完成");
        } else if (STATUS_ACCOMPLISH == status) {
            statusText = "已处理";
            bt_confirmOrCancel.setText("确认完成");
        } else if (STATUS_CONFIRM == status) {
            statusText = "已完成";
            bt_confirmOrCancel.setVisibility(View.GONE);
        }
        tv_status.setText(statusText);

        picUrls = mData.getPic_urls();
        if (picUrls.size() == 0) {
            rv_pic.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            GalleryAdapter mAdapter = new GalleryAdapter(this, picUrls);
            rv_pic.setAdapter(mAdapter);
            mAdapter.setOnItemClickLitener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirmOrCancel:
                if (status == STATUS_ACCOMPLISH || status == STATUS_UNDERWAY) {
                    new CommonDialog.Builder(this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定报修已完成？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    confirm();
                                }
                            }).show();
                } else {
                    new CommonDialog.Builder(MaintenanceDetailActivity.this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定取消报修？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancel();
                                }
                            }).show();
                }
                break;
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ArrayList<SquareImage> results = new ArrayList<>();
        for (int i = 0; i < picUrls.size(); i++) {
            results.add(new SquareImage("", picUrls.get(i), "", "", SquareImage.PhotoType.NETWORK));
        }
        NavigatorImage.startImageSwitcherActivity(MaintenanceDetailActivity.this, results, position, false, R.drawable.default_pic_item);
    }

    //取消报修
    private void cancel() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameters = RequestParameters.maintenanceOrderCancelOrConfirm(mData.getMaintenance_order_id()
                , loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.MAINTENANCE_ORDER_CANCEL).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
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
                if (response.isSuccess(MaintenanceDetailActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    //确认完成
    private void confirm() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameters = RequestParameters.maintenanceOrderCancelOrConfirm(mData.getMaintenance_order_id()
                , loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.MAINTENANCE_ORDER_CONFIRM).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
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
                if (response.isSuccess(MaintenanceDetailActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }


}
