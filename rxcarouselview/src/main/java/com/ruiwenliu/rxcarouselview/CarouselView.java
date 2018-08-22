package com.ruiwenliu.rxcarouselview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ruiwenliu.rxcarouselview.adapter.CarouselAdapter;
import com.ruiwenliu.rxcarouselview.inter.OnItemOnClickListener;
import com.ruiwenliu.rxcarouselview.inter.OnPageChangeListerer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ruiwen
 * Data:2018/8/16 0016
 * Email:1054750389@qq.com
 * Desc:只要是View 都可以轮播起来
 */

public class CarouselView<T extends View> extends RelativeLayout {
    private LinearLayout layCircle;
    private NoScrollViewPager viewPager;
    private int selectPostion;
    private CarouselAdapter mAdapter;
    private Context mContext;
    private Map<Integer, ImageView> viewMap = null;
    private int bottomSoildImg;//底部实心图片
    private int bottomHollow;//底部空心图片
    private Disposable mDisposable = null;
    private int dotWidth;//设置底部小图片宽度
    private int dotHeight;//设置底部小图片高度
    private int dotLeft;//设置底部小图片左边距；
    private int dotRight;//设置底部小图片右边距；
    private int beforeSelelct;
    private int carouselTime = 3;
    private boolean isStart;//是否启动轮播
    private int isLoop;//是否无限循环0 是 1否,默认是无限循环
    private OnItemOnClickListener itemOnClickListener;//点击事件
    private OnPageChangeListerer pageChangeListerer;//滑动监听事件

    public static final  int TYPE_LOOP_SCROLL=0;//无限滑动
    public static final  int TYPE_LOOP_SCROLL_NOT=1;//正常滑动
    public CarouselView(Context context) {
        super(context);
        init(context);
        mContext = context;
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mContext = context;
    }

    @Override
    protected void onDetachedFromWindow() {
        stopCarousel();
        if (mAdapter != null) {
            mAdapter.setOnItemClick(null);
        }
        super.onDetachedFromWindow();

    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_carouselly, this, true);
        layCircle = (LinearLayout) view.findViewById(R.id.lay_circly);
        viewPager = (NoScrollViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        stopCarousel();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isStart == true) {
                            startCarousel();
                        }
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 设置轮播数据
     *
     * @param list
     * @return
     */
    public CarouselView setCarouselData(final List<T> list) {
        if (list != null && list.size() > 0) {
            layCircle.setVisibility(GONE);
            mAdapter = new CarouselAdapter(list, isLoop);
            viewPager.setAdapter(mAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                     switch (isLoop){
                         case TYPE_LOOP_SCROLL:
                             selectPostion = position % list.size();
                             break;
                         case TYPE_LOOP_SCROLL_NOT:
                             selectPostion = position;
                             break;
                     }
                    if(pageChangeListerer!=null){
                        pageChangeListerer.onPageChange(selectPostion);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        return this;
    }


    /**
     * 设置加小圆点轮播数据
     *
     * @param list
     * @return
     */
    public CarouselView setDotCarouselData(final List<T> list, int imgSoild, int imgHollow) {
        if (list != null && list.size() > 0) {
            layCircle.setVisibility(VISIBLE);
            mAdapter = new CarouselAdapter(list, isLoop);
            bottomSoildImg = imgSoild <= 0 ? R.drawable.yuanquan_up2 : imgSoild;
            bottomHollow = imgHollow <= 0 ? R.drawable.yuanquan_down2 : imgHollow;
            setBottomImgAttribute(dotWidth <= 0 ? 20 : dotWidth, dotHeight <= 0 ? 20 : dotHeight, dotLeft <= 0 ? 10 : dotLeft, dotRight <= 0 ? 10 : dotRight);
            initBottomCircly(list.size());
            viewPager.setAdapter(mAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (isLoop){
                        case TYPE_LOOP_SCROLL:
                            selectPostion = position % list.size();
                            break;
                        case TYPE_LOOP_SCROLL_NOT:
                            selectPostion = position;
                            break;
                    }
                    setImgBack(selectPostion);
                    if(pageChangeListerer!=null){
                        pageChangeListerer.onPageChange(selectPostion);
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        return this;
    }


    /**
     * 设置小圆点
     *
     * @param selectItem
     */
    public void setImgBack(int selectItem) {
        if (viewMap != null && viewMap.size() > 0) {
            if (beforeSelelct != selectItem) {
                viewMap.get(selectItem).setImageResource(bottomSoildImg);
                viewMap.get(beforeSelelct).setImageResource(bottomHollow);
                beforeSelelct = selectItem;
            }
        }


    }


    /**
     * 初始化小圆点
     */
    private void initBottomCircly(int size) {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        viewMap.clear();
        if (layCircle.getChildCount() > 0) {
            layCircle.removeAllViews();
        }
        for (int i = 0; i < size; i++) {

            ImageView img = new ImageView(mContext);
            if (i == 0) {
                beforeSelelct = 0;
                img.setImageResource(bottomSoildImg);
            } else {
                img.setImageResource(bottomHollow);
            }
            viewMap.put(i, img);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            params.width = dotWidth;// 小点宽度
            params.height = dotHeight;// 小点高度
            params.leftMargin = dotLeft;// 小点的左边margin距离
            params.rightMargin = dotRight;// 小点的右边margin距离
            layCircle.addView(img, params);// 将小点添加到线性布局中

        }
    }


    /**
     * 开启自动轮播
     */
    public void startCarousel() {
        if (viewPager != null && mAdapter != null) {
            if (mDisposable == null || mDisposable.isDisposed()) {
                mDisposable = Observable.interval(carouselTime, carouselTime, TimeUnit.SECONDS)  // carouselTimes的延迟，carouselTimes的循环时间
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                isStart = true;
                                int currentIndex = viewPager.getCurrentItem();
                                if (++currentIndex == mAdapter.getCount()) {
                                    viewPager.setCurrentItem(0);

                                } else {
                                    viewPager.setCurrentItem(currentIndex, true);
                                }
                            }
                        });
            }
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopCarousel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    /**
     * 设置Item的点击事件
     */
    public void setOnItemClickListener(OnItemOnClickListener itemClickListener) {
        this.itemOnClickListener = itemClickListener;
        if (mAdapter != null) {
            mAdapter.setOnItemClick(itemOnClickListener);
        }
    }

    /**
     * 设置滑动的监听事件
     */
    public void setOnPageChangeListerer(OnPageChangeListerer pageChangeListerer) {
        this.pageChangeListerer = pageChangeListerer;
    }

    /**
     * 设置底部图片属性
     * 注意:如果要重新设置底部图片宽高和边距，请在初始化数据之前设置
     * 小于零则取默认值
     *
     * @param width  宽
     * @param height 高
     * @param left   左边距
     * @param right  右边距
     */
    public void setBottomImgAttribute(int width, int height, int left, int right) {
        dotWidth = width <= 0 ? dotWidth : width;
        dotHeight = height <= 0 ? dotHeight : height;
        dotLeft = left <= 0 ? dotLeft : left;
        dotRight = right <= 0 ? dotRight : right;
    }

    /**
     * 设置底部布局内部位置
     *
     * @param gravity
     */
    public void setBottomLayoutGravity(int gravity) {
        layCircle.setGravity(gravity);
    }


    /**
     * 设置底部布局高度
     *
     * @param height
     */
    public void setBottomLayoutHeight(int height) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = height;
        layCircle.setLayoutParams(params);
    }

    /**
     * 设置底部布局位置
     *
     * @param gravity
     */
    public void setBottomLayoutVerticalGravity(int gravity) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(gravity);
        layCircle.setLayoutParams(params);
    }

    /**
     * 设置底部布局外边距
     *
     * @param marginTop
     * @param marginBottom
     * @param marginLeft
     * @param marginRight
     */
    public void setBottomLayoutMain(int marginTop, int marginBottom, int marginLeft, int marginRight) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        layCircle.setLayoutParams(params);
    }

    /**
     * 设置底部背景颜色
     */
    public void setBottomBackgroundColor(int color) {
        layCircle.setBackgroundColor(color);
    }

    /**
     * 设置底部背景图片
     */
    public void setBottomBackgroundImg(int img) {
        layCircle.setBackgroundResource(img);
    }

    /**
     * 设置轮播时间
     *
     * @param num 单位为秒
     */
    public void setCarouselTime(int num) {
        this.carouselTime = num;
        stopCarousel();
        startCarousel();
    }

    /**
     * 设置底部小图片
     */
    public void setBottomImg(int imgSoild, int imgHollow) {
        this.bottomSoildImg = imgSoild;
        this.bottomHollow = imgHollow;
    }


    /**
     * 获取当前轮播的位置
     *
     * @return
     */
    public int getSelectPostion() {
        return selectPostion;
    }

    /**
     * 轮播图是否可以滑动
     *
     * @param isScroll
     */
    public void setCarouselViewScroll(boolean isScroll) {
        viewPager.setScroll(isScroll);
    }

    /**
     * 是否设置数据无限轮播，默认是无限轮播
     * 注：必须要在设置数据之前设置
     *
     * @param loop 0是无限，1是正常轮播
     */
    public void setLoop(int loop) {
        this.isLoop = loop;
    }

    /**
     * 重置刷新数据
     */
    public void resetData(List<T> list) {
        if (layCircle != null && viewPager != null) {
            viewPager.clearOnPageChangeListeners();
            mAdapter = null;
            if (layCircle.getVisibility() == VISIBLE) {
                setDotCarouselData(list, bottomSoildImg, bottomHollow);
                if (isStart == true) {
                    startCarousel();
                }
            } else {
                setCarouselData(list);
            }
            if (mAdapter != null && itemOnClickListener != null) {
                mAdapter.setOnItemClick(itemOnClickListener);
            }
        }
    }

}
