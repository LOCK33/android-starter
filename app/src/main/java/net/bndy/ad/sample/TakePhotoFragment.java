package net.bndy.ad.sample;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.CallbackHandler1;

public class TakePhotoFragment extends BaseFragment {

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mImageView = layout.findViewById(R.id.sample_take_photo_vi);
        layout.findViewById(R.id.sample_take_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).startTakePhoto(new CallbackHandler1<Bitmap>() {
                    @Override
                    public void callback(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        });

        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_take_photo;
    }
}
