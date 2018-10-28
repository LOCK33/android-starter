package net.bndy.ad.framework;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.bndy.ad.R;

import org.xutils.x;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String ACTION_EXIT = "action.exit";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SCAN_CODE = 2;

    private BaseActivity mThis;
    private Locale mCurrentLocale;
    private ExitReceiver mExitReceiver = new ExitReceiver();
    private Map<Integer, ContextMenuItemInfo> mContextMenuItemsMapping;
    private ProgressBarHandler mProgressBarHandler;
    protected Map<Integer, ContextMenuInfo> mViewsMappingWithContextMenu;

    public ApplicationUtils utils;

    // all callback here
    private CallbackHandler1<Bitmap> mTakePhotoCallbackHandler;
    private CallbackHandler1<String> mScanCallbackHandler;

    private @MenuRes int mMenu;

    public BaseActivity() {
        super();
        mThis = this;
        utils = new ApplicationUtils(mThis);
        mViewsMappingWithContextMenu = new HashMap<>();
        mContextMenuItemsMapping = new HashMap<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mTakePhotoCallbackHandler.callback(imageBitmap);
                    break;

                case REQUEST_SCAN_CODE:
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (scanResult != null) {
                        String result = scanResult.getContents();
                        mScanCallbackHandler.callback(result);
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

    public void registerProgressBar() {
        mProgressBarHandler = new ProgressBarHandler(this);
    }
    public void showProgressBar() {
        mProgressBarHandler.show();
    }
    public void hideProgressBar() {
        mProgressBarHandler.hide();
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

    public void startTakePhoto(CallbackHandler1<Bitmap> callback) {
        mTakePhotoCallbackHandler = callback;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    public void startScan(CallbackHandler1<String> callback) {
        mScanCallbackHandler = callback;
        IntentIntegrator integrator = new IntentIntegrator(this)
                .setCaptureActivity(CaptureActivity.class)
                .setPrompt(getResources().getString(R.string.hint_for_scan_code))
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    }
}
