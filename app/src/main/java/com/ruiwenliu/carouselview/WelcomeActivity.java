package com.ruiwenliu.carouselview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruiwenliu.rxcarouselview.CarouselView;
import com.ruiwenliu.rxcarouselview.inter.OnPageChangeListerer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.welcome_carouselview)
    CarouselView welcomeCarouselview;
    @BindView(R.id.tv_action)
    TextView tvAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        welcomeCarouselview.setBottomBackgroundImg(0);
        welcomeCarouselview.setDotCarouselData(getList(),R.drawable.selector, R.drawable.select_no);
        welcomeCarouselview.setOnPageChangeListerer(new OnPageChangeListerer() {
            @Override
            public void onPageChange(int i) {
                tvAction.setVisibility(i==2? View.VISIBLE:View.GONE);
            }
        });

    }

    @OnClick(R.id.tv_action)
    public void onViewClicked() {
        startActivity(new Intent(this, MainActivity.class));
    }


    /**
     * 获取数据
     *
     * @return
     */
    private List<ImageView> getList() {
        List<ImageView> list = new ArrayList<>();
        list.add(setDataToPhotoView("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534824807829&di=9ef4df042cb2bfc1a42e9243801baefa&imgtype=0&src=http%3A%2F%2Fimg4.cache.netease.com%2Fphoto%2F0026%2F2014-07-29%2FA2A3C8SK513O0026.jpg"));
        list.add(setDataToPhotoView("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534824807828&di=36ee950c0e97a030ac828f9715a15b68&imgtype=0&src=http%3A%2F%2Fimage14.m1905.cn%2Fuploadfile%2F2016%2F0822%2F20160822042056894099_watermark.jpg"));
        list.add(setDataToPhotoView("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534824807828&di=01c25d775b1e973e7dc056cd66776806&imgtype=0&src=http%3A%2F%2Fpic.eastlady.cn%2Fuploads%2Ftp%2F201605%2F10%2F31.jpg"));
        return list;
    }

    /**
     * 临时设置 可以写成公共方法
     *
     * @param url 地址这些数据应该是后台传给你的，这里只是做标识用
     * @return
     */
    private ImageView setDataToPhotoView(String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_image, null);
        Glide.with(this).load(url).into(imageView);
        return imageView;
    }


}
