# CarouselView
CarouselView 是一个自定义view,是rxJava +viewpager 组成万能的view轮播控件
这是依赖
    implementation 'com.github.Liuruiwen:CarouselView:1.0'
效果展示，如图所示
<div>
   <img src="https://github.com/Liuruiwen/CarouselView/blob/master/picture/device-2018-08-23-140144.gif" width=800 height=800/>
</div>
<div>
    <p>
    只要是view 都可以轮播！
 /**
       * 设置轮播数据
       -1是指小于0则取默认的小圆点，否则取你自己的轮播圆点图
         */
    </p>
     <p>
         actMainCv.setDotCarouselData(getListImgview(),-1,-1); 
       //轮播Item的点击事件
        actMainCv.setOnItemClickListener(new OnItemOnClickListener() {
            @Override
            public void ItemOnClick(View view, int postion) {
                Toast.makeText(MainActivity.this, "瑞文："+postion, Toast.LENGTH_SHORT).show();
            }
        });
    </p>
     <p>
      除此之外，还可以在性能上做处理，减少不必要的负荷
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
    </p>

</div>

       

    有兴趣的可以下载看看
