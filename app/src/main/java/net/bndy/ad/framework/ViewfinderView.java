package net.bndy.ad.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class ViewfinderView extends com.journeyapps.barcodescanner.ViewfinderView {

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        int lineLength = 50;
        int lineWidth = 10;
        paint.setAlpha(255);
        // top left corner
        canvas.drawRect(framingRect.left, framingRect.top, framingRect.left + lineLength, framingRect.top + lineWidth, paint);
        canvas.drawRect(framingRect.left, framingRect.top, framingRect.left + lineWidth, framingRect.top + lineLength, paint);

        // top right corner
        canvas.drawRect(framingRect.right - lineLength, framingRect.top, framingRect.right, framingRect.top + lineWidth, paint);
        canvas.drawRect(framingRect.right - lineWidth, framingRect.top, framingRect.right, framingRect.top + lineLength, paint);

        // bottom left corner
        canvas.drawRect(framingRect.left, framingRect.bottom - lineLength, framingRect.left + lineWidth, framingRect.bottom, paint);
        canvas.drawRect(framingRect.left, framingRect.bottom - lineWidth, framingRect.left + lineLength, framingRect.bottom, paint);

        // bottom right corner
        canvas.drawRect(framingRect.right - lineLength, framingRect.bottom - lineWidth, framingRect.right, framingRect.bottom, paint);
        canvas.drawRect(framingRect.right - lineWidth, framingRect.bottom - lineLength, framingRect.right, framingRect.bottom, paint);


        // change status position under framing
        TextView statusView = getRootView().findViewById(com.google.zxing.client.android.R.id.zxing_status_view);
        statusView.setTranslationY(framingRect.bottom + 20);
        statusView.setVisibility(VISIBLE);
    }
}
