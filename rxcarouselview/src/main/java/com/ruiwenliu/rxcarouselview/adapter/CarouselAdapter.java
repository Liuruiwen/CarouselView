package com.ruiwenliu.rxcarouselview.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import com.ruiwenliu.rxcarouselview.inter.OnItemOnClickListener;

import java.util.List;

/**
 * Created by ruiwen
 * Data:2018/8/16 0016
 * Email:1054750389@qq.com
 * Desc:
 */

public class CarouselAdapter<T extends View> extends PagerAdapter {
    private List<T> list;
    private OnItemOnClickListener listener;
    private int dataSize;
    public CarouselAdapter(List<T> list) {
        this.list = list;
    }
    public CarouselAdapter(List<T> list,int size) {
        this.list = list;
        this.dataSize=size;
    }
    @Override
    public int getCount() {
        return dataSize>0?list.size():Integer.MAX_VALUE;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(final ViewGroup container, final int position) {
        final int index = position % list.size();
        final T  view = list.get(index);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.ItemOnClick(v,index);
                }

            }
        });

        container.removeView(view);
        container.addView(view);
//        //在主线程添加
//        container.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                container.removeView(view);
//                container.addView(view);
//            }
//        });

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
    public int getSize(){
        return list.size();
    }

    public void setOnItemClick(OnItemOnClickListener listener){
        this.listener=listener;
    }

}
