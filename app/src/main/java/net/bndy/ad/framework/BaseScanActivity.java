package net.bndy.ad.framework;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.bndy.ad.R;

public abstract class BaseScanActivity extends BaseActivity {

    protected void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this)
                .setCaptureActivity(CaptureActivity.class)
                .setPrompt(getResources().getString(R.string.hint_for_scan_code))
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            scanCallback(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected abstract void scanCallback(String scanResult);
}
