package net.sunniwell.picturerecycle;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/1.
 */

public class PictureSlideLayout extends FrameLayout {
    private static final String TAG = "jpd-psL";
    private List<PagerBean> mPagerBeanList;
    private Context mContext;
    private LinearLayout mllDot;
    private ViewPager mPager;
    private List<View> mViewList;
    private int count;
    private long autoPlayTime;
    private int currentItem;
    private int dotSpace;
    private int dotSize;
    private Animator bigAmi;
    private Animator smallAmi;
    private SparseBooleanArray isLarge;
    private Runnable mTastk = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = (currentItem + 1) % (count + 2);
                mPager.setCurrentItem(currentItem);
                mHandler.postDelayed(mTastk, autoPlayTime);
            } else {
                mHandler.postDelayed(mTastk, 5000);
            }
        }
    };
    private Handler mHandler;
    private boolean isAutoPlay;

    public PictureSlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(TAG, "PictureSlideLayout: ");
    }

    public PictureSlideLayout(Context context) {
        this(context, null);
        Log.d(TAG, "PictureSlideLayout: ");
    }

    public PictureSlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "PictureSlideLayout: ");
        this.mContext = context;
        initView();
        initData();
        initAnimator();
    }

    private void initAnimator() {
        bigAmi = AnimatorInflater.loadAnimator(mContext, R.animator.scale_to_large);
        smallAmi = AnimatorInflater.loadAnimator(mContext, R.animator.scale_to_small);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.title_pic_dot, this, true);
        mPager = (ViewPager)view.findViewById(R.id.view_pager);
        mllDot = (LinearLayout)view.findViewById(R.id.ll_dot);
    }

    private void initData() {
        mPagerBeanList = new ArrayList<>();
        mViewList = new ArrayList<>();
    }
    
    public void setAutoPlayTime(long time) {
        this.autoPlayTime = time;
    }

    public void setDotSpace(int space) {
        this.dotSpace = space;
    }

    public void setDotSize(int size) {
        this.dotSize = size;
    }

    private void setViewList() {
        count = mPagerBeanList.size();
        Log.d(TAG, "setViewList: count:" + count);
        for (int i = 0; i < count + 2; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.title_pic, null);
            ImageView image = (ImageView)view.findViewById(R.id.pager_pic);
            TextView text = (TextView)view.findViewById(R.id.pager_text);
            if (i == 0) {
                Glide.with(mContext).load(mPagerBeanList.get(count - 1).getPagerPic()).into(image);
                text.setText(mPagerBeanList.get(count - 1).getPagerTitle());
            } else if (i == count + 1) {
                Glide.with(mContext).load(mPagerBeanList.get(0).getPagerPic()).into(image);
                text.setText(mPagerBeanList.get(0).getPagerTitle());
            } else {
                Glide.with(mContext).load(mPagerBeanList.get(i - 1).getPagerPic()).into(image);
                text.setText(mPagerBeanList.get(i - 1).getPagerTitle());
            }
            mViewList.add(view);
        }
    }

    private void setDotView() {
        isLarge = new SparseBooleanArray();
        for (int i = 0; i < count; i++) {
            View view = new View(mContext);
            view.setBackgroundResource(R.drawable.dot_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
            params.leftMargin = dotSpace / 2;
            params.rightMargin = dotSpace / 2;
            params.topMargin = dotSpace / 2;
            params.bottomMargin = dotSpace / 2;
            mllDot.addView(view, params);
            isLarge.put(i, false);
        }
        View v = mllDot.getChildAt(0);
        v.setBackgroundResource(R.drawable.dot_selected);
        bigAmi.setTarget(v);
        bigAmi.start();
        isLarge.put(0, true);
    }

    public void commit() {
        Log.d(TAG, "commit: ");
        setViewList();
        setDotView();
        mPager.setAdapter(new MyPagerAdapter());
        currentItem = 1;
        mPager.setCurrentItem(1);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG, "onPageScrolled: ");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: position:" + position);
                for (int i = 0; i < mllDot.getChildCount(); i++) {
                    if (i == position - 1) { // 选中状态
                        Log.d(TAG, "onPageScrolled: select..");
                        mllDot.getChildAt(i).setBackgroundResource(R.drawable.dot_selected);
                        if (!isLarge.get(i)) {
                            isLarge.put(i, true);
                            bigAmi.setTarget(mllDot.getChildAt(i));
                            bigAmi.start();
                        }
                    } else { // 未选中状态
                        mllDot.getChildAt(i).setBackgroundResource(R.drawable.dot_unselected);
                        if (isLarge.get(i)) {
                            isLarge.put(i, false);
                            smallAmi.setTarget(mllDot.getChildAt(i));
                            smallAmi.start();
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: state:" + state);
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        currentItem = mPager.getCurrentItem();
                        Log.d(TAG, "onPageScrollStateChanged: before set curItem:" + currentItem);
                        if (currentItem == count + 1) {
                            mPager.setCurrentItem(1, false);
                        }
                        if (currentItem == 0) {
                            mPager.setCurrentItem(count, false);
                        }
                        currentItem = mPager.getCurrentItem();
                        isAutoPlay = true;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;
                }
            }
        });
        startAutoPlay();
    }

    private void startAutoPlay() {
        Log.d(TAG, "startAutoPlay: ");
        if (count < 2) {
            isAutoPlay = false;
        } else {
            isAutoPlay = true;
            mHandler = new Handler();
            mHandler.postDelayed(mTastk, autoPlayTime);
        }
    }

    public void setPagerData(String title, String pic) {
        PagerBean pagerBean = new PagerBean();
        pagerBean.setPagerTitle(title);
        pagerBean.setPagerPic(pic);
        mPagerBeanList.add(pagerBean);
    }

    public void releaseResource() {
        mHandler.removeCallbacksAndMessages(null);
        mContext = null;
    }

    public class MyPagerAdapter extends PagerAdapter {
        private static final String TAG = "jpd-MyPagerAdapter";

        @Override
        public int getCount() {
            Log.d(TAG, "getCount: ");
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            Log.d(TAG, "isViewFromObject: ");
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.d(TAG, "instantiateItem: position:" + position);
            View view = mViewList.get(position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: position:" + position);
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, position - 1);
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(TAG, "destroyItem: position:" + position);
            Log.d(TAG, "destroyItem: object:" + object);
            container.removeView(mViewList.get(position));
        }
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
