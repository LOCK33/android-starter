package net.bndy.ad.framework;

import android.view.MenuItem;
import android.widget.AdapterView;

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
        this.title = title;
    }

    public interface OnContextMenuItemSelect {
        void onSelect(AdapterView.AdapterContextMenuInfo adapterContextMenuInfo, MenuItem menuItem, ContextMenuItemInfo contextMenuItemInfo);
    }
}
