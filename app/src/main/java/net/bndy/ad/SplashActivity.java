package net.bndy.ad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.bndy.ad.framework.ui.BaseActivity;
import net.bndy.ad.framework.utils.ImageUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {

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

        checkRedirect();
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
                    if (mDotViews != null && mDotViews.length > idx) {
                        mDotViews[idx].setSelected(idx == i ? true : false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressBar();
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkRedirect();
    }

    private void checkRedirect() {
        // skip and go to main
        if (Application.SPLASH_MODE == Application.SPLASH_MODE.NEVER ||
                (Application.SPLASH_MODE == Application.SPLASH_MODE.ONCE && getSP().getBoolean(Application.SP_KEY_SKIP_SPLASH, false))) {
            startActivity(MainActivity.class);
        }
    }

    private void initViews() {
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        mImageViews = new ArrayList<>();
        for (int res : Application.SPLASH_IMAGES) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(layoutParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageUtils.loadInto(res, iv);
            mImageViews.add(iv);
        }

        // init dots
        if (Application.SPLASH_IMAGES.length > 1) {
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
        } else {
            mEntryButton.setVisibility(View.VISIBLE);
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
