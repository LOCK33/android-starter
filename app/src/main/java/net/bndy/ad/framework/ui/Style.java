package net.bndy.ad.framework.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Style {
    private Context mContext;
    private Integer fontSize;
    private Integer width;
    private Integer height;
    private Drawable icon;
    private Drawable background;
    private Integer color;

    protected Context getContext() {
        return mContext;
    }

    public Integer getColor() {
        return color;
    }

    public Style setColor(Integer color) {
        this.color = color;
        return this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public Style setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public Style setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public Style setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Style setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public Drawable getBackground() {
        return background;
    }

    public Style setBackground(Drawable background) {
        this.background = background;
        return this;
    }

    public Style(Context context) {
        mContext = context;
    }

}
