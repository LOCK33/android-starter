package net.bndy.ad.framework.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.baoyz.swipemenulistview.SwipeMenuItem;

import net.bndy.ad.framework.utils.ApplicationUtils;

public class MenuItem {

    private String title;
    private Drawable icon;
    private Style style;

    public String getTitle() {
        return title;
    }

    public MenuItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public MenuItem setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public MenuItem setStyle(Style style) {
        this.style = style;
        return this;
    }

    public SwipeMenuItem toSwipeMenuItem(Context context) {
        SwipeMenuItem menuItem = new SwipeMenuItem(context);
        if (getTitle() != null) {
            menuItem.setTitle(getTitle());
        }
        if (getIcon() !=null) {
            menuItem.setIcon(getIcon());
        }
        if (style != null) {
            if (style.getFontSize() != null) {
                menuItem.setTitleSize(style.getFontSize());
            } else {
                // MUST set title size, otherwise the title does not display
                menuItem.setTitleSize(18);
            }
            if (style.getColor() != null) {
                menuItem.setTitleColor(style.getColor());
            }
            if (style.getWidth() != null) {
                menuItem.setWidth(ApplicationUtils.dip2px(context, style.getWidth()));
            }
            if (style.getBackground() != null) {
                menuItem.setBackground(style.getBackground());
            }
        }
        return menuItem;
    }
}
