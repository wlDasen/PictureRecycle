package net.sunniwell.picturerecycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private PictureSlideLayout mPSLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        initData();
        mPSLayout.setAutoPlayTime(5000);
        mPSLayout.setDotSize(12);
        mPSLayout.setDotSpace(12);
        mPSLayout.setOnItemClickListener(new PictureSlideLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("jpd", "onItemClick: position:" + position);
            }
        });
        mPSLayout.commit();
    }

    private void initWidgets() {
        mPSLayout = (PictureSlideLayout)findViewById(R.id.picture_slide_layout);
        mPSLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jpd-MA", "onClick: ");
            }
        });
    }
    private void initData() {
        String[] imageUrls = {"http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg",
                "http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg",
                "http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg",
                "http://pic1.zhimg.com/b6f59c017b43937bb85a81f9269b1ae8.jpg",
                "http://pic2.zhimg.com/a62f9985cae17fe535a99901db18eba9.jpg"};
        String[] titles = {"读读日报 24 小时热门 TOP 5 · 余文乐和「香港贾玲」乌龙绯闻",
                "写给产品 / 市场 / 运营的数据抓取黑科技教程",
                "学做这些冰冰凉凉的下酒宵夜，简单又方便",
                "知乎好问题 · 有什么冷门、小众的爱好？",
                "欧洲都这么发达了，怎么人均收入还比美国低"};
        for (int i = 0; i < 5; i++) {
            mPSLayout.setPagerData(titles[i], imageUrls[i]);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        mPSLayout.releaseResource();
        super.onDetachedFromWindow();
    }
}
