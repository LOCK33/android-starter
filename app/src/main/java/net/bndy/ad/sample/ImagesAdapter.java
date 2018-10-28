package net.bndy.ad.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.bndy.ad.R;
import net.bndy.ad.framework.ResourceInfo;

import java.util.List;

public class ImagesAdapter  extends ArrayAdapter<ResourceInfo> {

    private Context mContext;
    private int mItemViewId;

    public ImagesAdapter(Context context, @LayoutRes int itemViewId, List<ResourceInfo> imageIds) {
        super(context, itemViewId, imageIds);
        mContext = context;
        mItemViewId = itemViewId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ResourceInfo item = getItem(position);
        View layout = LayoutInflater.from(mContext).inflate(mItemViewId, parent, false);
        ImageView imageView = layout.findViewById(R.id.sample_images_item_iv);
        imageView.setImageDrawable((Drawable) item.tryGet());

        TextView textView = layout.findViewById(R.id.sample_images_item_tv);
        textView.setText(item.getName());

        return layout;
    }
}
