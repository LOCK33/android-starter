package net.bndy.ad.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.bndy.ad.R;
import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.ResourceInfo;
import net.bndy.ad.framework.ui.ListViewWithSwipeMenu;
import net.bndy.ad.framework.ui.MenuItem;
import net.bndy.ad.framework.ui.ResStyle;

public class ImagesListFragment extends BaseFragment {

    private ImagesAdapter mImagesAdapterForList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mImagesAdapterForList = new ImagesAdapter(this.getContext(), R.layout.item_images_list,
                utils.getResources(R.drawable.class));
        final ListViewWithSwipeMenu listViewWithSwipeMenu = layout.findViewById(R.id.sample_images_list);
        listViewWithSwipeMenu.setAdapter(mImagesAdapterForList);

        listViewWithSwipeMenu.setMenuItems(new MenuItem[] {
                new MenuItem().setTitle("VIEW")
                        .setStyle(new ResStyle(this.getContext())
                        .setBackgroundRes(R.color.colorLight)
                        .setColorRes(R.color.colorWhite)
                        .setWidthRes(R.dimen.width_swipe_menu_item))
                , new MenuItem().setIcon(utils.getDrawable(R.drawable.ic_clear_white_24dp))
                    .setStyle(new ResStyle(this.getContext())
                    .setBackgroundRes(R.color.colorDanger)
                    .setWidthRes(R.dimen.width_swipe_menu_item))
        });
        listViewWithSwipeMenu.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ResourceInfo resourceInfo = mImagesAdapterForList.getItem(position);
                switch (index) {
                    case 0:
                        utils.alert("#" + String.valueOf(resourceInfo.getId()), "R.drawable." + resourceInfo.getName(), null);
                        return false;

                    case 1:
                        // delete
                        utils.confirm("Delete", "Are you sure to remove " + resourceInfo.getName() + "?", new ApplicationUtils.Action() {
                            @Override
                            public void execute(Object... args) {
                                listViewWithSwipeMenu.smoothCloseMenu();
                            }
                        }, null);
                        return true;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_images_list;
    }
}
