package net.bndy.ad.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.bndy.ad.R;
import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.ResourceInfo;
import net.bndy.ad.framework.ui.ListViewWithSwipeMenu;
import net.bndy.ad.framework.ui.MenuItem;
import net.bndy.ad.framework.ui.ResStyle;

import java.util.List;

public class DrawableListFragment extends BaseFragment  {

    private ImageAdapter mImageAdapterForGrid;
    private ImageAdapter mImageAdapterForList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mImageAdapterForGrid = new ImageAdapter(this.getContext(), R.layout.item_drawable_grid,
                utils.getResources(R.drawable.class));

//        GridView gridView = layout.findViewById(R.id.sample_drawable_gird);
//        gridView.setAdapter(mImageAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ResourceInfo resourceInfo = mImageAdapter.getItem(position);
//                showItemDetail(resourceInfo);
//
//            }
//        });
//
//        // init context menus
//        ContextMenuInfo cmi = new ContextMenuInfo(R.id.sample_drawable_gird);
//        ContextMenuItemInfo cmii = new ContextMenuItemInfo(getResources().getString(R.string.view_detail));
//        cmii.setOnSelect(new ContextMenuItemInfo.OnContextMenuItemSelect() {
//            @Override
//            public void onSelect(AdapterView.AdapterContextMenuInfo adapterContextMenuInfo, MenuItem menuItem, ContextMenuItemInfo contextMenuItemInfo) {
//                showItemDetail(mImageAdapter.getItem(adapterContextMenuInfo.position));
//            }
//        });
//        cmi.addMenuItem(cmii);
//        registerForContextMenu(cmi);

        mImageAdapterForList = new ImageAdapter(this.getContext(), R.layout.item_drawable_list,
                utils.getResources(R.drawable.class));
        final ListViewWithSwipeMenu listViewWithSwipeMenu = layout.findViewById(R.id.sample_drawable_list);
        listViewWithSwipeMenu.setAdapter(mImageAdapterForList);

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
                ResourceInfo resourceInfo = mImageAdapterForList.getItem(position);
                switch (index) {
                    case 0:
                        showItemDetail(resourceInfo);
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
        return R.layout.fragment_drawable;
    }

    public class ImageAdapter extends ArrayAdapter<ResourceInfo> {

        private Context mContext;
        private int mItemViewId;

        public ImageAdapter(Context context, @LayoutRes int itemViewId, List<ResourceInfo> imageIds) {
            super(context, itemViewId, imageIds);
            mContext = context;
            mItemViewId = itemViewId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ResourceInfo item = getItem(position);
            View layout = LayoutInflater.from(mContext).inflate(mItemViewId, parent, false);
            ImageView imageView = layout.findViewById(R.id.sample_drawable_item_iv);
            imageView.setImageDrawable((Drawable) item.tryGet());

            TextView textView = layout.findViewById(R.id.sample_drawable_item_tv);
            textView.setText(item.getName());

            return layout;
        }
    }

    private void showItemDetail(ResourceInfo resourceInfo) {
        utils.alert("#" + String.valueOf(resourceInfo.getId()), "R.drawable." + resourceInfo.getName(), null);
    }
}
