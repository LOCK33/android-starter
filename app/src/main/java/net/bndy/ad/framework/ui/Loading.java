package net.bndy.ad.framework.ui;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import net.bndy.ad.R;

public class Loading {

    private Context mContext;
    private ProgressBar mProgressBar;
    private RelativeLayout mRelativeLayout;

    public Loading(Context context) {
        mContext = context;

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();

        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        mProgressBar.setIndeterminate(true);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        mRelativeLayout = new RelativeLayout(context);
        mRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorProgressBarBg));

        mRelativeLayout.setGravity(Gravity.CENTER);
        mRelativeLayout.addView(mProgressBar);

        layout.addView(mRelativeLayout, params);

        hide();
    }

    public void show() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRelativeLayout.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mRelativeLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }
}
