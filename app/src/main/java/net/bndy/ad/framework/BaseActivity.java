package net.bndy.ad.framework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import net.bndy.ad.framework.exception.UnsupportedViewException;

import org.xutils.x;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String ACTION_EXIT = "action.exit";

    private BaseActivity mThis;
    private Locale mCurrentLocale;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private Map<Integer, ContextMenuInfo.ContextMenuItemInfo> mContextMenuItemsMapping;
    private ProgressBarHandler mProgressBarHandler;
    protected ApplicationUtils mApplicationUtils;
    protected Map<Integer, ContextMenuInfo> mViewsMappingWithContextMenu;

    private @MenuRes int mMenu;

    public BaseActivity() {
        super();
        mApplicationUtils = new ApplicationUtils(this);
        mViewsMappingWithContextMenu = new HashMap<>();
        mContextMenuItemsMapping = new HashMap<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mThis = this;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_EXIT);
        registerReceiver(mExitReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mExitReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = mApplicationUtils.getLocale();

        if (!locale.equals(mCurrentLocale)) {
            mCurrentLocale = locale;
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenu > 0) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(mMenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (mViewsMappingWithContextMenu.containsKey(v.getId())) {
            ContextMenuInfo cmi = mViewsMappingWithContextMenu.get(v.getId());
            if (cmi.getOnCreateItems() != null) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                cmi.setMenuItems(cmi.getOnCreateItems().onCreate(adapterContextMenuInfo));
            }
            for (ContextMenuInfo.ContextMenuItemInfo contextMenuItemInfo : cmi.getMenuItems()) {
                mContextMenuItemsMapping.put(contextMenuItemInfo.getId(), contextMenuItemInfo);
                menu.add(contextMenuItemInfo.getGroupId(), contextMenuItemInfo.getId(), contextMenuItemInfo.getOrderId(), contextMenuItemInfo.getTitle());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mContextMenuItemsMapping.containsKey(item.getItemId())) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ContextMenuInfo.ContextMenuItemInfo contextMenuItemInfo = mContextMenuItemsMapping.get(item.getItemId());
            if (contextMenuItemInfo != null && contextMenuItemInfo.getOnSelect() != null) {
                contextMenuItemInfo.getOnSelect().onSelect(adapterContextMenuInfo, item, contextMenuItemInfo);
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    protected void registerForContextMenu(ContextMenuInfo contextMenuInfo) {
        mViewsMappingWithContextMenu.put(contextMenuInfo.getTargetId(), contextMenuInfo);
        registerForContextMenu(findViewById(contextMenuInfo.getTargetId()));
    }

    protected Bitmap generateBarcode(BarcodeFormat barcodeFormat, int width, int height) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap("content", barcodeFormat, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void setActionMenu(@MenuRes int menu) {
        mMenu = menu;
    }

    protected void exitApplication() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(ACTION_EXIT);
        BaseActivity.this.sendBroadcast(intent);
    }

    protected void info(@StringRes int message) {
        mApplicationUtils.info(message);
    }
    protected void info(String message) {
        mApplicationUtils.info(message);
    }
    protected void alert(@StringRes int title, @StringRes int message, ApplicationUtils.Action action) {
        mApplicationUtils.alert(title, message, action);
    }
    protected void alert(String title, String message, ApplicationUtils.Action action) {
        mApplicationUtils.alert(title, message, action);
    }
    protected void confirm(@StringRes int title, @StringRes int message, ApplicationUtils.Action actionYes, ApplicationUtils.Action actionNo) {
        mApplicationUtils.confirm(title, message, actionYes, actionNo);
    }

    protected void registerProgressBar() {
        mProgressBarHandler = new ProgressBarHandler(this);
    }
    protected void showProgressBar() {
        mProgressBarHandler.show();
    }
    protected void hideProgressBar() {
        mProgressBarHandler.hide();
    }

    protected void bindObjectToView(Object data) {
        bindObjectToView(data, null);
    }
    protected void bindObjectToView(Object data, String viewIdPrefix) {
        Field[] fields = data.getClass().getDeclaredFields();

        for(Field field: fields) {
            int elemId = this.getResources().getIdentifier((viewIdPrefix == null ? "" : viewIdPrefix) + field.getName().toLowerCase(), "id", this.getPackageName());
            if (elemId > 0) {
                View view = findViewById(elemId);
                if (view != null) {
                    String fieldValue = "";
                    try {
                        field.setAccessible(true);
                        fieldValue = field.get(data).toString();
                    } catch (IllegalAccessException ex) {
                        System.out.print(ex);
                    }

                    if (view instanceof TextView) {
                        ((TextView) view).setText(fieldValue);
                    } else if (view instanceof EditText) {
                        ((EditText) view).setText(fieldValue);
                    } else if (view instanceof ImageView) {
                        x.image().bind((ImageView) view, fieldValue);
                    } else if (view instanceof CheckBox) {
                        if (fieldValue == "true") {
                            ((CheckBox) view).setChecked(true);
                        }
                    }
                }
            }
        }
    }

    protected boolean checkRequired(@IdRes int viewId, @StringRes int requiredMessage) {
        View view = findViewById(viewId);
        if (view instanceof EditText) {
            if (checkRequired(((EditText) view).getText().toString(), requiredMessage)) {
                return true;
            }
            view.requestFocus();
            view.requestFocusFromTouch();
            return false;
        }

        throw new UnsupportedViewException(getResources().getResourceName(viewId));
    }
    protected boolean checkRequired(String val, @StringRes int requiredMessage) {
        if (val == null || val.trim().isEmpty()) {
            info(requiredMessage);
            return false;
        }
        return true;
    }

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    }
}
