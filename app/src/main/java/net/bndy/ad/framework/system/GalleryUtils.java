package net.bndy.ad.framework.system;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import net.bndy.ad.framework.RequestCodes;

public class GalleryUtils {

    public static boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    RequestCodes.GALLERY);
            return false;
        }
        return true;
    }

    public static void choosePhoto(Activity activity) {
        if (checkPermission(activity)) {
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            intentToPickPic.setType("image/*");
            intentToPickPic.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intentToPickPic, RequestCodes.GALLERY);
        }
    }
}
