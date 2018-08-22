package com.ruiwenliu.carouselview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ruiwenliu.rxcarouselview.CarouselView;
import com.ruiwenliu.rxcarouselview.inter.OnItemOnClickListener;
import com.ruiwenliu.rxcarouselview.inter.OnPageChangeListerer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewCarouselActivity extends AppCompatActivity {

    @BindView(R.id.carousel_view)
    CarouselView carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_carousel);
        ButterKnife.bind(this);
        /**
         * 注意======这是设置是否循环，必须在设置数据之前设置
         * 0是无限循环，1是正常轮播图片
         */
        carouselView.setLoop(1);
        carouselView.setCarouselData(getList());
        carouselView.setOnPageChangeListerer(new OnPageChangeListerer() {
            @Override
            public void onPageChange(int postion) {
                Toast.makeText(ViewCarouselActivity.this, "view:"+postion, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private List<View>  getList(){

        List<View>  list=new ArrayList<>();

        View viewOne= LayoutInflater.from(this).inflate(R.layout.item_view_onely,null,false);
        viewOne.findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewCarouselActivity.this, "我是view1", Toast.LENGTH_SHORT).show();
            }
        });
        View viewTwo= LayoutInflater.from(this).inflate(R.layout.item_view_twoly,null,false);
        View viewThree= LayoutInflater.from(this).inflate(R.layout.item_view_threely,null,false);
        list.add(viewOne);
        list.add(viewTwo);
        list.add(viewThree);
        return list;
    }

    @OnClick({R.id.btn_scroll_not, R.id.btn_scroll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scroll_not:
                carouselView.setCarouselViewScroll(false);
                break;
            case R.id.btn_scroll:
                carouselView.setCarouselViewScroll(true);
                break;
        }
    }
}
