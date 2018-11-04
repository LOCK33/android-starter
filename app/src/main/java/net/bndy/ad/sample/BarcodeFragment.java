package net.bndy.ad.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.CallbackHandler1;

public class BarcodeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        layout.findViewById(R.id.scan_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().startScan(new CallbackHandler1<String>() {
                    @Override
                    public void callback(String arg) {
                        if (arg != null) {
                            getBaseActivity().info(arg);
                        } else {
                            getBaseActivity().info("No result scanned");
                        }
                    }
                });

            }
        });
        final ImageView imageView = layout.findViewById(R.id.barcode_iv);
        layout.findViewById(R.id.barcode_generate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(utils.generateBarcode(
                        BarcodeFormat.QR_CODE, 200, 200
                ));
            }
        });
        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_barcode;
    }
}
