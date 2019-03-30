package net.bndy.ad.framework.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.bndy.ad.Application;
import net.bndy.ad.framework.utils.ApplicationUtils;
import net.bndy.ad.framework.utils.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseFragment extends Fragment {

    private View mLayout;
    private BaseActivity mActivity;
    private Map<Integer, ContextMenuItemInfo> mContextMenuItemsMapping;
    protected Map<Integer, ContextMenuInfo> mViewsMappingWithContextMenu;

    public ApplicationUtils utils;

    public BaseFragment() {
        mViewsMappingWithContextMenu = new HashMap<>();
        mContextMenuItemsMapping = new HashMap<>();
    }

    public SharedPreferencesHelper getSP() {
        return Application.getInstance().getSP();
    }

    public BaseActivity getBaseActivity() {
        return this.mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mActivity = (BaseActivity) this.getActivity();
        utils = this.mActivity.utils;
        mLayout = inflater.inflate(getLayout(), container, false);
        return mLayout;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (mViewsMappingWithContextMenu.containsKey(v.getId())) {
            ContextMenuInfo cmi = mViewsMappingWithContextMenu.get(v.getId());
            if (cmi.getOnCreateItems() != null) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                cmi.setMenuItems(cmi.getOnCreateItems().onCreate(adapterContextMenuInfo));
            }
            for (ContextMenuItemInfo contextMenuItemInfo : cmi.getMenuItems()) {
                mContextMenuItemsMapping.put(contextMenuItemInfo.getId(), contextMenuItemInfo);
                menu.add(contextMenuItemInfo.getGroupId(), contextMenuItemInfo.getId(), contextMenuItemInfo.getOrderId(), contextMenuItemInfo.getTitle());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mContextMenuItemsMapping.containsKey(item.getItemId())) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ContextMenuItemInfo contextMenuItemInfo = mContextMenuItemsMapping.get(item.getItemId());
            if (contextMenuItemInfo != null && contextMenuItemInfo.getOnSelect() != null) {
                contextMenuItemInfo.getOnSelect().onSelect(adapterContextMenuInfo, item, contextMenuItemInfo);
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public void registerForContextMenu(ContextMenuInfo contextMenuInfo) {
        mViewsMappingWithContextMenu.put(contextMenuInfo.getTargetId(), contextMenuInfo);
        registerForContextMenu(mLayout.findViewById(contextMenuInfo.getTargetId()));
    }

    protected abstract int getLayout();
}
