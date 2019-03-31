package net.bndy.ad.framework.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
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

import net.bndy.ad.Application;
import net.bndy.ad.framework.CallbackHandler1;
import net.bndy.ad.framework.RequestCodes;
import net.bndy.ad.framework.utils.ApplicationUtils;
import net.bndy.ad.framework.utils.ImageUtils;
import net.bndy.ad.framework.utils.SharedPreferencesHelper;
import net.bndy.ad.framework.system.CameraUtils;
import net.bndy.ad.framework.system.GalleryUtils;

import org.xutils.x;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String ACTION_EXIT = "action.exit";

    private BaseActivity mThis;
    private Application mApplication;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private Locale mCurrentLocale;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private Map<Integer, ContextMenuItemInfo> mContextMenuItemsMapping;
    private Loading mLoading;
    protected Map<Integer, ContextMenuInfo> mViewsMappingWithContextMenu;
    private String mCameraFilePath;

    public ApplicationUtils utils;

    // all callback here
    private CallbackHandler1<Bitmap> mTakePhotoCallbackHandler;
    private CallbackHandler1<Bitmap> mTakePhotoDefaultCallbackHandler;
    private CallbackHandler1<Uri> mChoosePhotoCallbackHandler;

    private @MenuRes int mMenu;

    public SharedPreferencesHelper getSP() {
        return this.mSharedPreferencesHelper;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mThis = this;
        this.utils = new ApplicationUtils(mThis);
        this.mViewsMappingWithContextMenu = new HashMap<>();
        this.mContextMenuItemsMapping = new HashMap<>();
        this.mApplication = (Application) getApplicationContext();
        this.mSharedPreferencesHelper = mApplication.getSP();

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
        Locale locale = utils.getLocale();

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
            for (ContextMenuItemInfo contextMenuItemInfo : cmi.getMenuItems()) {
                mContextMenuItemsMapping.put(contextMenuItemInfo.getId(), contextMenuItemInfo);
                menu.add(contextMenuItemInfo.getGroupId(), contextMenuItemInfo.getId(), contextMenuItemInfo.getOrderId(), contextMenuItemInfo.getTitle());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mContextMenuItemsMapping.containsKey(item.getItemId())) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ContextMenuItemInfo contextMenuItemInfo = mContextMenuItemsMapping.get(item.getItemId());
            if (contextMenuItemInfo != null && contextMenuItemInfo.getOnSelect() != null) {
                contextMenuItemInfo.getOnSelect().onSelect(adapterContextMenuInfo, item, contextMenuItemInfo);
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        CameraUtils.handleScanCodeResult(requestCode, resultCode, data);

        FileInputStream fis = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.CAMERA:
                    Bundle extras = data.getExtras();
                    // default to back thumbnail photo
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mTakePhotoDefaultCallbackHandler.callback(imageBitmap);
                    break;

                case RequestCodes.CAMERA_BACK_ORIGIN:
                    Bitmap cameraBitmap = ImageUtils.load(mThis, mCameraFilePath);
                    mTakePhotoCallbackHandler.callback(cameraBitmap);
                    break;

                case RequestCodes.GALLERY:
                    if (data != null) {
                        Uri contentURI = data.getData();
                        mChoosePhotoCallbackHandler.callback(contentURI);
                    }
                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registerForContextMenu(ContextMenuInfo contextMenuInfo) {
        mViewsMappingWithContextMenu.put(contextMenuInfo.getTargetId(), contextMenuInfo);
        registerForContextMenu(findViewById(contextMenuInfo.getTargetId()));
    }

    public void startActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void setActionMenu(@MenuRes int menu) {
        mMenu = menu;
    }

    public void setIcon(@DrawableRes int id) {
        setIcon(utils.getDrawable(id));
    }

    public void setIcon(Drawable drawable) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(drawable);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    public void exitApplication() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(ACTION_EXIT);
        BaseActivity.this.sendBroadcast(intent);
    }

    public void info(@StringRes int message) {
        utils.info(message);
    }
    public void info(String message) {
        utils.info(message);
    }
    public void alert(@StringRes int title, @StringRes int message, ApplicationUtils.Action action) {
        utils.alert(title, message, action);
    }
    public void alert(String title, String message, ApplicationUtils.Action action) {
        utils.alert(title, message, action);
    }
    public void confirm(@StringRes int title, @StringRes int message, ApplicationUtils.Action actionYes, ApplicationUtils.Action actionNo) {
        utils.confirm(title, message, actionYes, actionNo);
    }
    public void confirm(@StringRes int title, @StringRes int message, ApplicationUtils.Action actionYes) {
        utils.confirm(title, message, actionYes, null);
    }

    public void registerProgressBar() {
        mLoading = new Loading(this);
    }
    public void showProgressBar() {
        mLoading.show();
    }
    public void hideProgressBar() {
        mLoading.hide();
    }

    public void bindObjectToView(Object data) {
        bindObjectToView(data, null);
    }
    public void bindObjectToView(Object data, String viewIdPrefix) {
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

    public boolean checkRequired(@IdRes int viewId, @StringRes int requiredMessage) {
        return utils.checkRequired(this, viewId, requiredMessage);
    }
    public boolean checkRequired(String val, @StringRes int requiredMessage) {
        return utils.checkRequired(val, requiredMessage);
    }

    public void startChoosePhoto(CallbackHandler1<Uri> callback) {
        mChoosePhotoCallbackHandler = callback;
        GalleryUtils.choosePhoto(this);
    }

    public void startTakePhoto(CallbackHandler1<Bitmap> callback) {
        mTakePhotoCallbackHandler = callback;
        mCameraFilePath = CameraUtils.takePhoto(this, false);
    }

    public void startTakeThumbnailPhoto(CallbackHandler1<Bitmap> callback) {
        mTakePhotoDefaultCallbackHandler = callback;
        mCameraFilePath = CameraUtils.takePhoto(this, true);
    }

    public void startScan(CallbackHandler1<String> callback) {
        CameraUtils.scanCode(this, "default", callback);
    }

    public void startScan(String requestCode, CallbackHandler1<String> callback) {
        CameraUtils.scanCode(this, requestCode, callback);
    }

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    }
}
