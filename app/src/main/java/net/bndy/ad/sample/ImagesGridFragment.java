package net.bndy.ad.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import net.bndy.ad.R;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.ContextMenuInfo;
import net.bndy.ad.framework.ContextMenuItemInfo;
import net.bndy.ad.framework.ResourceInfo;

public class ImagesGridFragment extends BaseFragment {

    private ImagesAdapter mImagesAdapterForGrid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mImagesAdapterForGrid = new ImagesAdapter(this.getContext(), R.layout.item_images_grid,
                utils.getResources(R.drawable.class));

        GridView gridView = layout.findViewById(R.id.sample_images_gird);
        gridView.setAdapter(mImagesAdapterForGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResourceInfo resourceInfo = mImagesAdapterForGrid.getItem(position);
                utils.alert("#" + String.valueOf(resourceInfo.getId()), "R.drawable." + resourceInfo.getName(), null);
            }
        });

        // init context menus
        ContextMenuInfo cmi = new ContextMenuInfo(R.id.sample_images_gird);
        ContextMenuItemInfo cmii = new ContextMenuItemInfo(getResources().getString(R.string.view_detail));
        cmii.setOnSelect(new ContextMenuItemInfo.OnContextMenuItemSelect() {
            @Override
            public void onSelect(AdapterView.AdapterContextMenuInfo adapterContextMenuInfo, android.view.MenuItem menuItem, ContextMenuItemInfo contextMenuItemInfo) {
                ResourceInfo resourceInfo = mImagesAdapterForGrid.getItem(adapterContextMenuInfo.position);
                utils.alert("#" + String.valueOf(resourceInfo.getId()), "R.drawable." + resourceInfo.getName(), null);
            }
        });
        cmi.addMenuItem(cmii);
        registerForContextMenu(cmi);
        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_images_grid;
    }
}
