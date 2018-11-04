package net.bndy.ad.framework.system;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import net.bndy.ad.Application;

public class DownloadUtils {

    private static DownloadManager sDownloadManager;
    private static Application sApplication;

    static {
        sApplication = Application.getInstance();
        sDownloadManager = (DownloadManager) sApplication.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static Integer getStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor c = sDownloadManager.query(query);
        if (c.moveToFirst()) {
            return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
        }
        return null;
    }

    public static long download(String uri, String title, String description) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setAllowedOverMetered(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(title);
        request.setDescription(description);
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath(), sApplication.getPackageName());
        long downloadId = sDownloadManager.enqueue(request);
        Application.SP.set(Application.SP_KEY_UPGRADE_ID, downloadId);
        return downloadId;
    }

    public static int remove(long downloadId) {
        return sDownloadManager.remove(downloadId);
    }

    public static Uri getUriByDownloadId(long downloadId) {
        return sDownloadManager.getUriForDownloadedFile(downloadId);
    }
}
