package net.bndy.ad.framework;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

import net.bndy.ad.framework.helper.ImageHelper;

public class BlurTransformation implements Transformation {

    private Context mContext;
    private float mRadius;

    public BlurTransformation(Context context, float radius) {
        mContext = context;
        mRadius = radius;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageHelper.blur(mContext, bitmap, mRadius);
    }

    @Override
    public String key() {
        return "blur(" + String.valueOf(mRadius) + ")";
    }
}
