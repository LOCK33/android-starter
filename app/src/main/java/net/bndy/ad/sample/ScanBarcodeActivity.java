package net.bndy.ad.sample;

import android.os.Bundle;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseScanActivity;

public class ScanBarcodeActivity extends BaseScanActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        startScan();
    }

    @Override
    protected void scanCallback(String scanResult) {
        if (scanResult != null) {
            info(scanResult);
        } else {
            info("No result scanned");
        }
    }
}
