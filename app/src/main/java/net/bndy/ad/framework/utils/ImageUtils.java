package net.bndy.ad.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.DrawableRes;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.bndy.ad.framework.BlurTransformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ImageUtils {

    public static Bitmap resize(Context context, @DrawableRes int resourceId, int targetWidth, int targetHeight) {
        try {
            return Picasso.get().load(resourceId).resize(targetWidth, targetHeight).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap resize(Context context, String filepath, int targetWidth, int targetHeight) {
        try {
            return Picasso.get().load(filepath).resize(targetWidth, targetHeight).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap load(Activity activity, String filepath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // following 2 rows just get image size
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(filepath, options);

        WindowManager windowManager = activity.getWindowManager();
        Display windowDisplay = windowManager.getDefaultDisplay();
        int windowWidth = windowDisplay.getWidth();
        int scale = 1;
        if(options.outWidth>windowWidth){
            scale = options.outWidth / windowWidth;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(filepath, options);
    }


    public static void loadInto(Uri uri, ImageView imageView) {
        Picasso.get().load(uri).fit().centerCrop().into(imageView);
    }
    public static void loadInto(String imagePath, ImageView imageView) {
        Picasso.get().load(imagePath).fit().centerCrop().into(imageView);
    }
    public static void loadInto(@DrawableRes int res, ImageView imageView) {
        Picasso.get().load(res).fit().centerCrop().into(imageView);
    }

    public static Bitmap resize(Context context, Uri uri, int targetWidth, int targetHeight) {
        try {
            return Picasso.get().load(uri).resize(targetWidth, targetHeight).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String saveImage(Context context, Bitmap myBitmap, String nameWithRelativePath) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        String filepath = null;
        if (nameWithRelativePath == null || nameWithRelativePath.isEmpty()) {
            filepath = Environment.getExternalStorageDirectory() + File.separator
                    + Calendar.getInstance().getTimeInMillis() + ".png";
        } else {
            filepath = Environment.getExternalStorageDirectory()
                    + (nameWithRelativePath.startsWith(File.separator) ? File.separator : "")
                    + nameWithRelativePath;
        }
        File file = new File(filepath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{file.getPath()},
                    new String[]{"image/png"}, null);
            fo.close();
            return file.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
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
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
