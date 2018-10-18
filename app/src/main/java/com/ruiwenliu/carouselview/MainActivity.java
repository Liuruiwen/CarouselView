package com.ruiwenliu.carouselview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ruiwenliu.rxcarouselview.CarouselView;
import com.ruiwenliu.rxcarouselview.inter.OnItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.act_main_cv)
    CarouselView actMainCv;
    @BindView(R.id.et_time)
    EditText etTime;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //        actMainCv.setCarouselData(getListImgview());  //这是不添加小圆点
        /**
         * -1是指小于0则取默认的小圆点，否则取你自己的轮播圆点图
         */
        actMainCv.setDotCarouselData(getListImgview(),-1,-1);
        actMainCv.setOnItemClickListener(new OnItemOnClickListener() {
            @Override
            public void ItemOnClick(View view, int postion) {
                Toast.makeText(MainActivity.this, "瑞文："+postion, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(actMainCv!=null){ //当activity 不可见时，可以停止滚动，增加app性能，减少不必要的负荷
            actMainCv.stopCarousel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(actMainCv!=null){//当activity 可见时，开启滚动
            actMainCv.startCarousel();
        }
    }


    @OnClick({R.id.tv_start, R.id.tv_stop, R.id.btn_time_set, R.id.btn_dot_postion,R.id.btn_reset,R.id.btn_carousel_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                actMainCv.startCarousel();
                break;
            case R.id.tv_stop:
                actMainCv.stopCarousel();
                break;
            case R.id.btn_time_set:
                String time=etTime.getText().toString().trim();
                if(TextUtils.isEmpty(time)){
                    Toast.makeText(this, "输入的延迟时间不能为空", Toast.LENGTH_SHORT).show();
                   return;
                }
                actMainCv.setCarouselTime(Integer.valueOf(time));
                break;
            case R.id.btn_dot_postion:
                actMainCv.setBottomLayoutGravity(Gravity.RIGHT);//
                break;
            case R.id.btn_reset:
                actMainCv.setBottomImg(R.drawable.selector,R.drawable.select_no);
                actMainCv.resetData(getResetData());
                break;
            case R.id.btn_carousel_view:
                   startActivity(new Intent(this,ViewCarouselActivity.class));
                break;
        }
    }
    

    /**
     * 添加轮播图片
     * 注====只要是View都可以轮播
     * 这里直接把后台的数据放到Imageview上就可以了
     *
     * @return
     */
    private List<ImageView> getListImgview() {
        List<ImageView> list = new ArrayList<>();
        list.add(setDataToPhotoView("http://imgsrc.baidu.com/forum/w=580/sign=1dc0f5cbaaec08fa260013af69ef3d4d/6c8876c2d56285350f1e83559aef76c6a6ef6325.jpg"));
        list.add(setDataToPhotoView("http://i0.hdslb.com/video/86/8642147023ca8eef937cfba25de85358.jpg"));
        list.add(setDataToPhotoView("http://04.imgmini.eastday.com/mobile/20180729/20180729080013_2e0e4f43723e01e40b81bee9e4d4d4e9_1.jpeg"));
        list.add(setDataToPhotoView("http://pic.wenwo.com/fimg/66712100936_553.jpg"));
        return list;
    }


    /**
     * 获取重置的数据
     * @return
     */
    private List<ImageView> getResetData(){
        List<ImageView> list = new ArrayList<>();
        list.add(setDataToPhotoView("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534824807829&di=9ef4df042cb2bfc1a42e9243801baefa&imgtype=0&src=http%3A%2F%2Fimg4.cache.netease.com%2Fphoto%2F0026%2F2014-07-29%2FA2A3C8SK513O0026.jpg"));
        list.add(setDataToPhotoView("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534824807828&di=36ee950c0e97a030ac828f9715a15b68&imgtype=0&src=http%3A%2F%2Fimage14.m1905.cn%2Fuploadfile%2F2016%2F0822%2F20160822042056894099_watermark.jpg"));
        list.add(setDataToPhotoView("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534824807828&di=01c25d775b1e973e7dc056cd66776806&imgtype=0&src=http%3A%2F%2Fpic.eastlady.cn%2Fuploads%2Ftp%2F201605%2F10%2F31.jpg"));
        return list;
    }

    /**
     * 临时设置 可以写成公共方法
     * @param url  地址这些数据应该是后台传给你的，这里只是做标识用
     * @return
     */
    private ImageView setDataToPhotoView( String url) {
       ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_image, null);

        Glide.with(this).load(url).into(imageView);
        return imageView;
    }

}
