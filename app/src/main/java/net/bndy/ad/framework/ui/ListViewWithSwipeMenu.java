package net.bndy.ad.framework.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.List;

public class ListViewWithSwipeMenu extends SwipeMenuListView {

    public ListViewWithSwipeMenu(Context context) {
        super(context);
    }

    public ListViewWithSwipeMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewWithSwipeMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMenuItems(final MenuItem... menuItems) {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                for (MenuItem menuItem: menuItems) {
                    menu.addMenuItem(menuItem.toSwipeMenuItem(getContext()));
                }
            }
        };
        this.setMenuCreator(swipeMenuCreator);
    }

    public void setDiffMenuItems(final MenuCreator menuCreator) {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                for (MenuItem menuItem: menuCreator.create(menu)) {
                    menu.addMenuItem(menuItem.toSwipeMenuItem(getContext()));
                }
            }
        };
        this.setMenuCreator(swipeMenuCreator);
    }

    public interface MenuCreator {
        List<MenuItem> create(SwipeMenu swipeMenu);
    }
}
