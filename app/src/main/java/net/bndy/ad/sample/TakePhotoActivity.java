package net.bndy.ad.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.framework.CallbackHandler;
import net.bndy.ad.framework.CallbackHandler1;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class TakePhotoActivity extends BaseActivity  {

    @ViewInject(R.id.sample_take_photo_vi)
    private ImageView mImageView;

    @Event(R.id.sample_take_photo_btn)
    private void onTakePhoto(View view) {
        startTakePhoto(new CallbackHandler1<Bitmap>() {
            @Override
            public void callback(Bitmap bitmap) {
                mImageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        x.view().inject(this);
    }
}
