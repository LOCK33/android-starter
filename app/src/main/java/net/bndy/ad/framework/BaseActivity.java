package net.bndy.ad.framework;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import net.bndy.ad.ApplicationUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    private Locale mCurrentLocale;

    protected ApplicationUtils applicationUtils;
    protected Map<Integer, ContextMenuInfo> viewsMappingWithContextMenu;
    private Map<Integer, ContextMenuInfo.ContextMenuItemInfo> contextMenuItemsMapping;

    public BaseActivity() {
        super();
        applicationUtils = new ApplicationUtils(this);
        viewsMappingWithContextMenu = new HashMap<>();
        contextMenuItemsMapping = new HashMap<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = applicationUtils.getLocale();

        if (!locale.equals(mCurrentLocale)) {
            mCurrentLocale = locale;
            recreate();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (viewsMappingWithContextMenu.containsKey(v.getId())) {
            ContextMenuInfo cmi = viewsMappingWithContextMenu.get(v.getId());
            if (cmi.getOnCreateItems() != null) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                cmi.setMenuItems(cmi.getOnCreateItems().onCreate(adapterContextMenuInfo));
            }
            for (ContextMenuInfo.ContextMenuItemInfo contextMenuItemInfo : cmi.getMenuItems()) {
                contextMenuItemsMapping.put(contextMenuItemInfo.getId(), contextMenuItemInfo);
                menu.add(contextMenuItemInfo.getGroupId(), contextMenuItemInfo.getId(), contextMenuItemInfo.getOrderId(), contextMenuItemInfo.getTitle());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (contextMenuItemsMapping.containsKey(item.getItemId())) {
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ContextMenuInfo.ContextMenuItemInfo contextMenuItemInfo = contextMenuItemsMapping.get(item.getItemId());
            if (contextMenuItemInfo != null && contextMenuItemInfo.getOnSelect() != null) {
                contextMenuItemInfo.getOnSelect().onSelect(adapterContextMenuInfo, item, contextMenuItemInfo);
            }
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    protected void registerForContextMenu(ContextMenuInfo contextMenuInfo) {
        viewsMappingWithContextMenu.put(contextMenuInfo.getTargetId(), contextMenuInfo);
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
}
