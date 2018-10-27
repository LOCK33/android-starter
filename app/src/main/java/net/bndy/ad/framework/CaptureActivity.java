package net.bndy.ad.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.Size;
//import com.journeyapps.barcodescanner.ViewfinderView;

import net.bndy.ad.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class CaptureActivity extends BaseActivity {

    private CaptureManager capture;
    private int mHeightScannerView;
    private int mHeightSurfaceView;

    @ViewInject(R.id.decorated_barcode_view)
    private DecoratedBarcodeView mDecoratedBarcodeView;
    @ViewInject(R.id.zxing_status_view)
    private TextView mStatusView;
    @ViewInject(R.id.zxing_barcode_surface)
    private BarcodeView mBarcodeView;
    @ViewInject(R.id.zxing_viewfinder_view)
    private ViewfinderView mViewfinderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_capture);

        x.view().inject(this);

//        switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);
//
//        // if the device does not have flashlight in its camera,
//        // then remove the switch flashlight button...
//        if (!hasFlash()) {
//            switchFlashlightButton.setVisibility(View.GONE);
//        }

        capture = new CaptureManager(this, mDecoratedBarcodeView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        ViewTreeObserver viewTreeObserver = mDecoratedBarcodeView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeightScannerView = mDecoratedBarcodeView.getHeight();
//                mHeightSurfaceView = mBarcodeView.getFramingRectSize().height;
            }
        });
//        mStatusView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mStatusView.getLayoutParams();
//                layoutParams.setMargins(0, 0, 0, (mHeightScannerView + mHeightSurfaceView) / 2);
//                mStatusView.setLayoutParams(layoutParams);
//                return false;
//            }
//        });
//        mViewfinderView.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                mHeightSurfaceView = mBarcodeView.getFramingRectSize().height;
//                return false;
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        BarcodeView barcodeView = findViewById(R.id.zxing_barcode_surface);
//        barcodeView.setFramingRectSize(new Size(300, 100));
//        TextView statusText = findViewById(R.id.zxing_status_view);
//        ConstraintLayout constraintLayout =  new ConstraintLayout(this);
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(constraintLayout);
//        constraintSet.connect(R.id.zxing_status_view,ConstraintSet.TOP,R.id.zxing_barcode_surface,ConstraintSet.BOTTOM,0);
//        constraintSet.applyTo(constraintLayout);
//
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDecoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
