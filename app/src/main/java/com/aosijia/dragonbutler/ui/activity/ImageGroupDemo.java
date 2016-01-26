package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.TouchDelegate;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.imagegroup.utils.ImageGroupDisplayHelper;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wanglj on 15/12/23.
 */
public class ImageGroupDemo extends BaseActivity implements ImageGroupView.OnImageClickListener{

    private ImageGroupView imageGroupAddAble;
    private ImageGroupView imageGroup;
    private ImageGroupView imageGroup2;
    private SimpleDraweeView oneImage;
    private SimpleDraweeView oneImage2;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.image_group_demo);

        initView();
        setData();
    }

    private void initView() {
        imageGroup = (ImageGroupView) findViewById(R.id.images_group);
        imageGroup2 = (ImageGroupView) findViewById(R.id.images_group_2);
        imageGroupAddAble = (ImageGroupView) findViewById(R.id.images_group_addable);
        oneImage = (SimpleDraweeView) findViewById(R.id.image_one_show);
        oneImage2 = (SimpleDraweeView) findViewById(R.id.image_one_show2);
        text = (TextView) findViewById(R.id.text);
    }

    private void setData() {
        imageGroupAddAble.setFragmentManager(getSupportFragmentManager());
        imageGroupAddAble.setNetworkPhotos(createTestData());
        imageGroup.setNetworkPhotos(createTestData());
        imageGroup2.setNetworkPhotos(createTestData());



        imageGroup2.setLocalPhotos(Arrays.asList(new String[]{"/storage/emulated/0/Pictures/1447745372984.jpg"}));
        imageGroup2.setOnImageClickListener(this);
        imageGroup.setOnImageClickListener(this);

        ImageGroupDisplayHelper.displayImageOneShow(oneImage, "http://img1.3lian.com/img13/c3/82/d/64.jpg", getResources().getDimensionPixelSize(R.dimen.image_dimen));
        ImageGroupDisplayHelper.displayImageOneShow(oneImage2, "http://pic2.ooopic.com/10/23/79/75bOOOPICa3.jpg", getResources().getDimensionPixelSize(R.dimen.image_dimen));
        text.setText(Html.fromHtml("<strike>从首批发布的赞助商来看，全球云计算大会·中国站“本土化”成果显著，国内厂商的大力投入充分显示了对该平台的认可，也积极响应了国务院“积极开展国际合作、整合国际创新资源、加强国内外企业研发合作”的号召。战略合作伙伴、首席赞助商等独家权益早早被预订，一些新兴的云计算品牌也把大会作为商务和宣传重地，积极参与各项展示与合作。</strike>"));
    }

    private ArrayList<String> createTestData() {
        ArrayList<String> result = new ArrayList<>();
        result.add("http://img4.imgtn.bdimg.com/it/u=434281914,1810736344&fm=21&gp=0.jpg");
        result.add("http://img1.3lian.com/img13/c3/82/d/64.jpg");

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageGroupAddAble.onParentResult(requestCode, data);
        }
    }

    @Override
    public void onImageClick(SquareImage clickImage, ArrayList<SquareImage> squareImages, ArrayList<String> allImageInternetUrl) {
        Toast.makeText(this, "Inter Images is :  " + allImageInternetUrl, Toast.LENGTH_SHORT).show();
    }
}
