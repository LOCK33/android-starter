package net.bndy.ad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.bndy.ad.framework.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {

    private final static int[] IMAGES_RES = new int[]{R.drawable.splash_1, R.drawable.splash_2};
    private ArrayList<ImageView> mImageViews;
    private ImageView[] mDotViews;

    @ViewInject(R.id.splash_vp)
    private ViewPager mViewPager;
    @ViewInject(R.id.splash_layout_dot)
    private LinearLayout mDotLayout;

    @ViewInject(R.id.splash_btn_entry)
    private Button mEntryButton;
    @Event(R.id.splash_btn_entry)
    private void onEnter(View view) {
        showProgressBar();
        Application.getInstance().disableSplash();
        startActivity(MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        x.view().inject(this);
        registerProgressBar();

        initViews();
        mViewPager.setAdapter(new SplashPagerAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mEntryButton.setVisibility( i == mImageViews.size() - 1 ? View.VISIBLE : View.GONE);
                for(int idx = 0; idx < mImageViews.size(); idx++){
                    mDotViews[idx].setSelected(idx == i ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void initViews() {
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        mImageViews = new ArrayList<>();
        for (int res : IMAGES_RES) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(layoutParams);
            iv.setImageResource(res);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViews.add(iv);
        }

        // init dots
        LinearLayout.LayoutParams dotViewsLayoutParams = new LinearLayout.LayoutParams(30, 30);
        dotViewsLayoutParams.setMargins(10, 0, 10, 0);
        mDotViews = new ImageView[mImageViews.size()];
        for (int i = 0; i < mImageViews.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(dotViewsLayoutParams);
            imageView.setImageResource(R.drawable.selector_dot);
            imageView.setSelected( i == 0 ? true : false);
            mDotViews[i] = imageView;
            mDotLayout.addView(imageView);
        }
    }

    class SplashPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mImageViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }
    }
}
