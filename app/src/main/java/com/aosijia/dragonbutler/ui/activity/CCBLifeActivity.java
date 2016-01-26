package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.MD5;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 悦生活
 * Created by Jacky on 2016/1/7.
 * Version 1.0
 */
public class CCBLifeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_ccblife);

        setTitle("悦生活", null, NO_RES_ID);

        int margin = Uiutils.dip2px(this, 13);
        int width = Uiutils.getScreenWidth(this) - margin*2;
        int height = (int) (width/5.394);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(margin, margin, margin, 0);

        LoginResp resp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        final String city_code =resp.getData().getUser_info().getHousehold().getCity_code();
        findViewById(R.id.btn_life_service).setLayoutParams(params);
        findViewById(R.id.btn_life_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommonWebViewActivity.class);
                intent.putExtra("url", getUrl(city_code, "010"));
                intent.putExtra("title", "生活服务");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_travel).setLayoutParams(params);
        findViewById(R.id.btn_travel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommonWebViewActivity.class);
                intent.putExtra("url", getUrl(city_code, "020"));
                intent.putExtra("title", "娱乐出行");
                startActivity(intent);
            }
        });


        findViewById(R.id.btn_financial).setLayoutParams(params);
        findViewById(R.id.btn_financial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommonWebViewActivity.class);
                intent.putExtra("url",getUrl(city_code,"030"));
                intent.putExtra("title","金融理财");
                startActivity(intent);
            }
        });
    }


    private String getUrl(String city_code, String bill_type) {
        String url = "http://life.ccb.com/tran/WCCMainPlatV5?";

        long current_time = System.currentTimeMillis();
        HashMap<String, String> params = new HashMap<>();
        params.put("MERCHANTID", "YSH320000000004");
        params.put("BRANCHID", "320000000");
        params.put("MER_CHANNEL", "0");
        params.put("DATE", TimeUtils.getFourteenFormatTime(current_time).substring(0, 8));
        params.put("TIME", TimeUtils.getFourteenFormatTime(current_time).substring(8, 14));

        params.put("TXCODE", "520100");
        params.put("PROV_CODE", "320000");
        params.put("CITY_CODE", city_code);
        params.put("CCB_IBSVersion", "V5");
        params.put("SERVLET_NAME", "WCCMainPlatV5");
        String mac =  getSinatue(params);
        params.put("MAC",mac);
        params.put("BILL_TYPE",bill_type);
        url = url+getEncodeParameter(params);
        return url;

    }


    public String getSinatue(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("MERCHANTID="+params.get("MERCHANTID"));
        sb.append("BRANCHID="+params.get("BRANCHID"));
        sb.append("MER_CHANNEL="+params.get("MER_CHANNEL"));
        sb.append("DATE="+params.get("DATE"));
        sb.append("TIME="+params.get("TIME"));
        sb.append("263716941221556944139409");//保密字符串

        try {
            String md5Str = MD5.md5(sb.toString());
            return md5Str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEncodeParameter(Map<String, String> params){
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        StringBuilder encodedParams = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String,String> entry = (Map.Entry) it.next();
                encodedParams.append(entry.getKey());
            encodedParams.append("=");
            encodedParams.append(entry.getValue());
            encodedParams.append("&");
        }
//        .substring(0,encodedParams.toString().length()-1);
        return encodedParams.toString();
    }
}
