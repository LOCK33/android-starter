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

public class CameraHelper extends BaseHelper {



    public CameraHelper(Activity activity) {
        super(activity);
    }

    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RequestCodes.CAMERA);
            return false;
        }
        return true;
    }

    public void takePhotoForThumbnail() {
        if (checkPermission()) {
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intentToTakePhoto.resolveActivity(mActivity.getPackageManager()) != null) {
                mActivity.startActivityForResult(intentToTakePhoto, RequestCodes.CAMERA);
            }
        }
    }

    public String takePhoto() {
        if (checkPermission()) {
            String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filePath = Environment.getExternalStorageDirectory() + File.separator + filename;
            Uri imageUri = Uri.fromFile(new File(filePath));
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (intentToTakePhoto.resolveActivity(mActivity.getPackageManager()) != null) {
                mActivity.startActivityForResult(intentToTakePhoto, RequestCodes.CAMERA_BACK_ORIGIN);
            }

            return filePath;
        }
        return null;
    }
}
