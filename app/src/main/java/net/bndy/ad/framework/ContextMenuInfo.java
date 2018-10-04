package net.bndy.ad.framework;

import android.support.annotation.IdRes;
import android.view.MenuItem;
import android.widget.AdapterView;

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
        menuItems = Arrays.asList(menuItemInfos);
    }

    public void setOnCreateItems(OnCreateItems onCreateItems) {
        this.onCreateItems = onCreateItems;
    }

    public class ContextMenuItemInfo {

        private int id;
        private int groupId;
        private int orderId;
        private CharSequence title;
        private OnContextMenuItemSelect onSelect;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public CharSequence getTitle() {
            return title;
        }

        public void setTitle(CharSequence title) {
            this.title = title;
        }

        public OnContextMenuItemSelect getOnSelect() {
            return onSelect;
        }

        public void setOnSelect(OnContextMenuItemSelect onSelect) {
            this.onSelect = onSelect;
        }

        public ContextMenuItemInfo(CharSequence title) {
            title = title;
        }
    }

    public interface OnContextMenuItemSelect {
        void onSelect(AdapterView.AdapterContextMenuInfo adapterContextMenuInfo, MenuItem menuItem, ContextMenuItemInfo contextMenuItemInfo);
    }

    public interface OnCreateItems {
        List<ContextMenuItemInfo> onCreate(AdapterView.AdapterContextMenuInfo adapterContextMenuInfo);
    }
}
