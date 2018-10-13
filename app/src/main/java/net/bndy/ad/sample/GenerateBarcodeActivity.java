package net.bndy.ad.sample;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class GenerateBarcodeActivity extends BaseActivity {

    @ViewInject(R.id.generate_barcode_view)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_barcode);
        x.view().inject(this);


        imageView.setImageBitmap(generateBarcode(BarcodeFormat.QR_CODE, 300, 300));

    }
}
