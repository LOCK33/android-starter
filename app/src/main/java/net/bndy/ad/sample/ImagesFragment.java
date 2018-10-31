package net.bndy.ad.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.ui.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class ImagesFragment extends BaseFragment  {

    private TabLayout mTabLayout;
    private Map<Integer, Fragment> mFragmentMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mTabLayout = layout.findViewById(R.id.sample_images_tabs);

        mFragmentMap = new HashMap<>();
        mFragmentMap.put(R.string.list, new ImagesListFragment());
        mFragmentMap.put(R.string.grid, new ImagesGridFragment());
        mTabLayout.setItems(R.id.sample_images_tabcontent_container, mFragmentMap, this.getActivity());
        mTabLayout.showDot(0);
        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_images;
    }
}
