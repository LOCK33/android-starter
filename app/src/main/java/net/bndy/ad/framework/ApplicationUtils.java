package net.bndy.ad.framework;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import net.bndy.ad.R;
import net.bndy.ad.framework.exception.UnsupportedViewException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.blurry.Blurry;

public class ApplicationUtils {

    private final static String SP_KEY_LOCALE = "LOCALE";

    private Context mContext;

    public ApplicationUtils(Context context) {
        mContext = context;
    }

    public void info(int message) {
        info(mContext.getResources().getString(message));
    }

    public void info(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public AlertDialog.Builder alert(int title, int message, final Action aciton) {
        return alert(mContext.getResources().getString(title), mContext.getResources().getString(message), aciton);
    }

    public AlertDialog.Builder alert(String title, String message, final Action action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action != null) {
                    action.execute();
                }
            }
        });
        builder.show();
        return builder;
    }

    public AlertDialog.Builder confirm(int title, int message, Action actionYes, Action actionNo) {
        return confirm(mContext.getResources().getString(title), mContext.getResources().getString(message), actionYes, actionNo);
    }

    public AlertDialog.Builder confirm(String title, String message, Action actionYes) {
        return confirm(title, message, actionYes, null);
    }
    public AlertDialog.Builder confirm(String title, String message, final Action actionYes, final Action actionNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (actionNo != null) {
                    actionNo.execute();
                }
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (actionYes != null) {
                    actionYes.execute();
                }
            }
        });
        builder.show();
        return builder;
    }

    public AlertDialog.Builder promptInput(String title, String message, final Action actionYes, final Action actionNo) {
        final EditText edtText = new EditText(mContext);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = edtText.getText().toString();
                if (actionYes != null) {
                    actionYes.execute(value);
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (actionNo != null) {
                    actionNo.execute();
                }
            }
        });
        builder.show();
        return builder;
    }

    public Locale getLocale() {
        String locale = PreferenceManager.getDefaultSharedPreferences(mContext).getString(SP_KEY_LOCALE, null);
        if (locale != null && locale.indexOf("_") > 0) {
            String[] items = locale.split("_");
            return new Locale(items[0], items[1]);
        }
        return mContext.getResources().getConfiguration().locale;
    }

    public void setLocale(Locale locale) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sp.edit();
        if (locale != null) {
            editor.putString(SP_KEY_LOCALE, locale.toString());
            Locale.setDefault(locale);
        } else {
            editor.remove(SP_KEY_LOCALE);
        }
        editor.commit();

        Configuration config = mContext.getResources().getConfiguration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }

    public Drawable getDrawable(int id) {
        return getDrawable(id, mContext);
    }

    public List<ResourceInfo> getResources(Class<?> rClazz) {
        return getResources(rClazz, mContext);
    }

    public boolean checkRequired(Activity activity, @IdRes int viewId, @StringRes int requiredMessage) {
        View view = activity.findViewById(viewId);
        if (view instanceof EditText) {
            if (checkRequired(((EditText) view).getText().toString(), requiredMessage)) {
                return true;
            }
            view.requestFocus();
            view.requestFocusFromTouch();
            return false;
        }

        throw new UnsupportedViewException(mContext.getResources().getResourceName(viewId));
    }

    public boolean checkRequired(String val, @StringRes int requiredMessage) {
        if (val == null || val.trim().isEmpty()) {
            info(requiredMessage);
            return false;
        }
        return true;
    }

    public Bitmap generateBarcode(BarcodeFormat barcodeFormat, int width, int height) {
        return newBarcode(barcodeFormat, width, height);
    }

    public int dip2px(float dpValue) {
        return ApplicationUtils.dip2px(mContext, dpValue);
    }

    public int px2dip(float pxValue) {
        return ApplicationUtils.px2dip(mContext, pxValue);
    }

    public Spanned fromHtml(String html) {
        return ApplicationUtils.convertHtml(html);
    }

    public void setTextViewAsLink(TextView textView) {
        enableLinksInTextView(textView);
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    public void enableLinksInTextView(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void blurScreen() {
        blurScreen(25, 1, 500);
    }
    public void blurScreen(int radius, int sampling, int animate) {
        Activity activity = (Activity) mContext;
        blurScreenTo(activity, null, radius, sampling, animate);
    }

    public void bringToFront(View view) {
        view.bringToFront();
        ((ViewGroup) view.getParent()).invalidate();
    }

    // here to start static methods code

    public static Drawable getDrawable(int id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static List<ResourceInfo> getResources(Class<?> rClazz, Context context) {
        List<ResourceInfo> resourceInfos = new ArrayList<>();
        Field[] fields = rClazz.getDeclaredFields();
        for (Field field : fields) {
            int resID = context.getResources().getIdentifier(field.getName(), rClazz.getSimpleName(), context.getPackageName());
            resourceInfos.add(new ResourceInfo(context, resID, field.getName(), rClazz.getSimpleName()));
        }
        return resourceInfos;
    }

    public static int getColor(int id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, context.getTheme());
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Bitmap newBarcode(BarcodeFormat barcodeFormat, int width, int height) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap("content", barcodeFormat, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Spanned convertHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static void blurScreenTo(final Activity activity, final ViewGroup target) {
        blurScreenTo(activity, target, 25, 2, 500);
    }
    public static void blurScreenTo(final Activity activity, final ViewGroup target, final int radius, final int sampling, final int animate) {
        final ViewGroup rootView = (ViewGroup) (activity.getWindow().getDecorView().getRootView());
        final ViewGroup targetView = target == null ? rootView : target;
        rootView.post(new Runnable() {
            @Override
            public void run() {
                Blurry.with(activity)
                        .radius(radius)
                        .sampling(sampling)
                        .async()
                        .animate(animate)
                        .onto(targetView);
            }
        });
    }

    public interface Action {
        void execute(Object... args);
    }
}
