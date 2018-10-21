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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.framework.ResourceInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class DrawableListActivity extends BaseActivity {

    @ViewInject(R.id.sample_drawable_gird)
    private GridView mGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_list);
        x.view().inject(this);

        gv_init();
    }

    void gv_init() {
        final ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.drawable_list_item,
                mApplicationUtils.getResources(R.drawable.class));
        mGridView.setAdapter(imageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResourceInfo resourceInfo = imageAdapter.getItem(position);
                alert("#" + String.valueOf(resourceInfo.getId()), "R.drawable." + resourceInfo.getName(), null);
            }
        });
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
            ImageView imageView = layout.findViewById(R.id.sample_drawable_list_grid_item_iv);
            imageView.setImageDrawable((Drawable) item.tryGet());

            TextView textView = layout.findViewById(R.id.sample_drawable_list_grid_item_tv);
            textView.setText(item.getName());
            return layout;
        }
    }
}
