package net.bndy.ad.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.bndy.ad.R;
import net.bndy.ad.framework.ResourceInfo;

import java.util.List;

public class ImagesAdapter extends ArrayAdapter<ResourceInfo> {

    private int mItemViewId;

    public ImagesAdapter(Context context, @LayoutRes int itemViewId, List<ResourceInfo> resourceInfos) {
        super(context, itemViewId, resourceInfos);
        mItemViewId = itemViewId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), mItemViewId, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ResourceInfo item = getItem(position);
        holder.iv_icon.setImageDrawable((Drawable) item.tryGet());
        holder.tv_name.setText(item.getName());

        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        public ViewHolder(View view) {
            iv_icon = view.findViewById(R.id.sample_images_item_iv);
            tv_name = view.findViewById(R.id.sample_images_item_tv);
            view.setTag(this);
        }
    }
}
