package net.bndy.ad.framework;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public abstract class BaseScanActivity extends BaseActivity {

    protected void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            scanCallback(result);
        } else {
            scanCallback(null);
        }
    }

    protected abstract void scanCallback(String scanResult);
}
