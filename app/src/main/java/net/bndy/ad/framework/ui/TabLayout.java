package net.bndy.ad.framework.ui;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.Map;

public class TabLayout extends CommonTabLayout {

    public TabLayout(Context context) {
        super(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setItems(@IdRes int tabContentContainer, Map<Integer, Fragment> fragmentMap, FragmentActivity activity) {
        ArrayList<CustomTabEntity> tabs = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        for(final int title: fragmentMap.keySet()) {
            tabs.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return getResources().getString(title);
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            });
            fragments.add(fragmentMap.get(title));
        }

        this.setTabData(tabs, activity, tabContentContainer, fragments);
    }
}
