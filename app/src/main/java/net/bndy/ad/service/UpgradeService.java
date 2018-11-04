package net.bndy.ad.service;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import net.bndy.ad.Application;
import net.bndy.ad.R;
import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.system.DownloadUtils;

import java.io.File;

public class UpgradeService {

    private Application mApplication;

    public UpgradeService() {
        this.mApplication = Application.getInstance();
    }

    public long downloadApk() {
        long downloadId = DownloadUtils.download(Application.URL_UPGRADE,
                this.mApplication.getString(R.string.app_name) + ".apk",
                null);
        Application.SP.set(Application.SP_KEY_UPGRADE_ID, downloadId);
        return downloadId;
    }

    public void upgrade() throws PackageManager.NameNotFoundException {
        if (Application.URL_UPGRADE == null || Application.URL_UPGRADE.isEmpty()) {
            return;
        }

        // check download status
        long downloadId = getDownloadID();
        if (downloadId <= 0) {
            return;
        }

        switch (DownloadUtils.getStatus(downloadId)) {
            case DownloadManager.STATUS_FAILED:
            case DownloadManager.STATUS_PAUSED:
            case DownloadManager.STATUS_PENDING:
            case DownloadManager.STATUS_RUNNING:
                return;

            case DownloadManager.STATUS_SUCCESSFUL:
                String apkPath = getDownloadedApkPath();
                if (apkPath != null) {
                    PackageInfo packageInfo = getApkInfo(apkPath);
                    // check version
                    if (!ApplicationUtils.getVersionName(this.mApplication).equals(packageInfo.versionName)) {
                        Uri apkUri = getDownloadedApk();
                        if (apkUri != null) {
                            Intent intent = new Intent();
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            this.mApplication.startActivity(intent);
                        }
                    } else {
                        clear();
                    }
                }
                break;
        }
    }

    public void clear() {
        long downloadId = this.getDownloadID();
        if (downloadId > 0) {
            DownloadUtils.remove(downloadId);
            Application.SP.remove(Application.SP_KEY_UPGRADE_ID);
        }
    }

    public Uri getDownloadedApk() {
        long downloadId = this.getDownloadID();
        if (downloadId > 0) {
            return DownloadUtils.getUriByDownloadId(downloadId);
        }
        return null;
    }

    public String getDownloadedApkPath() {
       Uri apkUri = getDownloadedApk();
       if (apkUri != null) {
           return new File(apkUri.getPath()).toString();
       }
       return null;
    }

    public long getDownloadID() {
        return Application.SP.getLong(Application.SP_KEY_UPGRADE_ID, -1);
    }

    public PackageInfo getApkInfo(String path) {
        PackageManager pm = this.mApplication.getPackageManager();
        return pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
    }
}
