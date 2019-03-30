package net.bndy.ad.framework.ui;

import android.support.annotation.IdRes;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContextMenuInfo {

    @IdRes private int targetId;
    private List<ContextMenuItemInfo> menuItems;
    private OnCreateItems onCreateItems;

    public int getTargetId() {
        return targetId;
    }

    public List<ContextMenuItemInfo> getMenuItems() {
        return menuItems;
    }

    public OnCreateItems getOnCreateItems() {
        return onCreateItems;
    }

    public void setMenuItems(List<ContextMenuItemInfo> contextMenuItemInfos) {
        menuItems = contextMenuItemInfos;
    }

    public ContextMenuInfo(@IdRes int targetId, ContextMenuItemInfo...menuItemInfos) {
        this.targetId = targetId;
        int index = 0;
        for(ContextMenuItemInfo contextMenuItemInfo: menuItemInfos) {
            index++;
            contextMenuItemInfo.setId(targetId * 100 + index);
        }
        // Arrays.asList returns list can not use add(...) method, just remove permitted
        this.menuItems = new ArrayList<>(Arrays.asList(menuItemInfos));
    }

    public void setOnCreateItems(OnCreateItems onCreateItems) {
        this.onCreateItems = onCreateItems;
    }

    public void addMenuItem(ContextMenuItemInfo menuItemInfo) {
        this.menuItems.add(menuItemInfo);
    }

    public interface OnCreateItems {
        List<ContextMenuItemInfo> onCreate(AdapterView.AdapterContextMenuInfo adapterContextMenuInfo);
    }
}
