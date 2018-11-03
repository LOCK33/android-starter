package net.bndy.ad.framework.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.DrawableRes;

import com.squareup.picasso.Picasso;

import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BlurTransformation;

import java.io.IOException;

public class ImageHelper {

    public static Drawable changeSize(Context context, @DrawableRes int resourceId, int targetWidth, int targetHeight) {
        try {
            return bitmapToDrawable(context, Picasso.get().load(resourceId).resize(targetWidth, targetHeight).get());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap blur(Context context, Bitmap bitmap, float radius) {
        Bitmap blurredBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // Initialize RenderScript and the script to be used
        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation output = Allocation
                .createFromBitmap(renderScript, blurredBitmap);

        script.setInput(input);
        script.setRadius(radius);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        renderScript.destroy();
        return blurredBitmap;
    }

    public static Drawable blur(Context context, Bitmap bitmap) {
        return bitmapToDrawable(context, new BlurTransformation(context, 25).transform(bitmap));
    }

    public static Drawable blur(Context context, Drawable drawable) {
        return blur(context, drawableToBitmap(drawable));
    }

    public static Drawable blur(Context context, @DrawableRes int drawable) {
        return blur(context, ApplicationUtils.getDrawable(drawable, context));
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable)drawable).getBitmap();
    }
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
