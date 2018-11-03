package net.bndy.ad.framework.device;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import net.bndy.ad.framework.RequestCodes;

import java.io.File;

public class CameraHelper {

    private Activity mActivity;

    public CameraHelper(Activity activity) {
        mActivity = activity;
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

    private void takePhoto(String filename) {
        if (checkPermission()) {
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filePath = Environment.getExternalStorageDirectory() + File.separator + filename;
            /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
            Uri imageUri = FileProvider.getUriForFile(mActivity,
                    mActivity.getApplicationContext().getPackageName() + ".my.provider",
                    new File(filePath));
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            mActivity.startActivityForResult(intentToTakePhoto, RequestCodes.GALLERY);
        }
    }

    private void choosePhoto() {
        if (checkPermission()) {
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            mActivity.startActivityForResult(intentToPickPic, RequestCodes.GALLERY);
        }
    }
}
