package net.bndy.ad.framework.system;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.bndy.ad.R;
import net.bndy.ad.framework.CallbackHandler1;
import net.bndy.ad.framework.RequestCodes;
import net.bndy.ad.framework.ui.CaptureActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

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

    public static String takePhoto(Activity activity, boolean thumbnail) {
        if (checkPermission(activity)) {
            String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
            Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filePath = Environment.getExternalStorageDirectory() + File.separator + filename;
            Uri imageUri = Uri.fromFile(new File(filePath));
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (intentToTakePhoto.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intentToTakePhoto, thumbnail ? RequestCodes.CAMERA : RequestCodes.CAMERA_BACK_ORIGIN);
            }

            return filePath;
        }
        return null;
    }

    public static Map<String, CallbackHandler1<String>> scanHandlers = new HashMap<>();

    public static void scanCode(Activity activity, String requestCode, CallbackHandler1<String> callback) {
        scanHandlers.put(requestCode, callback);
        IntentIntegrator integrator = new IntentIntegrator(activity)
                .setCaptureActivity(CaptureActivity.class)
                .setPrompt(activity.getResources().getString(R.string.hint_for_scan_code))
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public static void handleScanCodeResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            CallbackHandler1<String> handler1 = scanHandlers.get(requestCode);
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null && handler1 != null) {
                String result = scanResult.getContents();
                handler1.callback(result);
            }
        }
    }
}
