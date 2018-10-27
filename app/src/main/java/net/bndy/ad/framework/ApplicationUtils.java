package net.bndy.ad.framework;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import net.bndy.ad.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public AlertDialog.Builder confirm(int title, int message, final Action actionYes, final Action actionNo) {
        return confirm(mContext.getResources().getString(title), mContext.getResources().getString(message), actionYes, actionNo);
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
        for(Field field:fields){
            int resID = context.getResources().getIdentifier(field.getName(), rClazz.getSimpleName(), context.getPackageName());
            resourceInfos.add(new ResourceInfo(context, resID, field.getName(), rClazz.getSimpleName()));
        }
        return resourceInfos;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public interface Action {
        void execute(Object... args);
    }
}
