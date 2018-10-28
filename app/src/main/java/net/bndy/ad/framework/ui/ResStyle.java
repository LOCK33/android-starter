package net.bndy.ad.framework.ui;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;

import net.bndy.ad.framework.ApplicationUtils;

public class ResStyle extends Style {

    public ResStyle(Context context) {
        super(context);
    }

    public ResStyle setBackgroundRes(@DrawableRes int background) {
        super.setBackground(ApplicationUtils.getDrawable(background, getContext()));
        return this;
    }
    public ResStyle setIconRes(@DrawableRes int icon) {
        super.setIcon(ApplicationUtils.getDrawable(icon, getContext()));
        return this;
    }

    public ResStyle setColorRes(@ColorRes int color) {
        super.setColor(color);
        return this;
    }

    public ResStyle setWidthRes(@DimenRes int width) {
        super.setWidth((int)getContext().getResources().getDimension(width));
        return this;
    }

    public ResStyle setHeightRes(@DimenRes int height) {
        super.setWidth((int)getContext().getResources().getDimension(height));
        return this;
    }

    public ResStyle setFontSize(@DimenRes int fontSize) {
        super.setFontSize((int)getContext().getResources().getDimension(fontSize));
        return this;
    }
}
