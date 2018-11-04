package net.bndy.ad.framework.system;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import net.bndy.ad.framework.RequestCodes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtils {
    public static boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RequestCodes.CAMERA);
            return false;
        }
        return true;
    }

    public static void takePhotoForThumbnail(Activity activity) {
        if (checkPermission(activity)) {
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intentToTakePhoto.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intentToTakePhoto, RequestCodes.CAMERA);
            }
        }
    }

    public static String takePhoto(Activity activity) {
        if (checkPermission(activity)) {
            String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filePath = Environment.getExternalStorageDirectory() + File.separator + filename;
            Uri imageUri = Uri.fromFile(new File(filePath));
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (intentToTakePhoto.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intentToTakePhoto, RequestCodes.CAMERA_BACK_ORIGIN);
            }

            return filePath;
        }
        return null;
    }
}
