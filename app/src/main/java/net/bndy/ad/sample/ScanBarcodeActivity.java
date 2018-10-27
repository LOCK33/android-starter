package net.bndy.ad.sample;

import android.os.Bundle;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.framework.CallbackHandler1;

public class ScanBarcodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        startScan(new CallbackHandler1<String>() {
            @Override
            public void callback(String arg) {
                if (arg != null) {
                    info(arg);
                } else {
                    info("No result scanned");
                }
            }
        });
    }
}
