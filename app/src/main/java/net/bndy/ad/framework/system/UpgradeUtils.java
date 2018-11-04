package net.bndy.ad.framework.system;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.annotations.SerializedName;

import net.bndy.ad.Application;
import net.bndy.ad.R;
import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.CallbackHandler1;
import net.bndy.ad.service.ApiRequest;

import java.io.File;

public class UpgradeUtils {

    private static final String SP_KEY_DOWNLOAD_ID = "upgrade.download.id";

    private static Application sApplication;

    static {
        sApplication = Application.getInstance();
    }

    public static void getUpgradeInfo(final CallbackHandler1<UpgradeInfo> callback, final CallbackHandler1<VolleyError> requestErrorCallback) {
        new ApiRequest<>(
                UpgradeInfo.class,
                Request.Method.GET,
                Application.URL_UPGRADE_METADATA, null,
                new Response.Listener<UpgradeInfo>() {
                    @Override
                    public void onResponse(UpgradeInfo response) {
                        callback.callback(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (requestErrorCallback !=null) {
                    requestErrorCallback.callback(error);
                } else {
                    error.printStackTrace();
                }
            }
        });
    }

    public static UpgradeStatus upgradeTo(UpgradeInfo upgradeInfo) {
        if (upgradeInfo == null || upgradeInfo.getAppVersion() == null || upgradeInfo.getUrl() == null) {
            new Error("No app_version/appVersion or url found.").printStackTrace();
            return UpgradeStatus.NO_METADATA;
        }

        final String currentVersion = ApplicationUtils.getAppVersion(sApplication);
        UpgradeStatus status = null;
        // check version from remote server
        if (!upgradeInfo.getAppVersion().equals(currentVersion)) {
            // need to upgrade
            long downloadId = getDownloadID();
            if (downloadId == -1 || DownloadUtils.getUriByDownloadId(downloadId) == null) {
                // need to download
                downloadId = downloadApk(upgradeInfo.getUrl());
                sApplication.getSP().set(SP_KEY_DOWNLOAD_ID, downloadId);
                status = UpgradeStatus.DOWNLOADING;
            } else {
                // install locally
                switch (DownloadUtils.getStatus(downloadId)) {
                    case DownloadManager.STATUS_FAILED:
                    case DownloadManager.STATUS_PAUSED:
                    case DownloadManager.STATUS_PENDING:
                    case DownloadManager.STATUS_RUNNING:
                        status = UpgradeStatus.DOWNLOADING;
                        break;

                    case DownloadManager.STATUS_SUCCESSFUL:
                        String apkPath = getDownloadedApkPath();
                        if (apkPath != null) {
                            PackageInfo packageInfo = getApkInfo(apkPath);
                            // check version with local package
                            if (!ApplicationUtils.getAppVersion(sApplication).equals(packageInfo.versionName)) {
                                install(getDownloadedApk());
                                status = UpgradeStatus.INSTALL_FROM_LOCAL;
                            } else {
                                status = UpgradeStatus.NEWEST;
                                clear();
                            }
                        } else {
                            clear();
                        }
                        break;
                }

            }
        } else {
            status = UpgradeStatus.NEWEST;
        }
        return status;
    }

    private static void clear() {
        long downloadId = getDownloadID();
        if (downloadId > 0) {
            DownloadUtils.remove(downloadId);
            sApplication.getSP().remove(SP_KEY_DOWNLOAD_ID);
        }
    }

    private static void install(Uri apkUri) {
        if (apkUri != null) {
            Intent intent = new Intent();
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sApplication.startActivity(intent);
        }
    }

    private static long downloadApk(String url) {
        long downloadId = DownloadUtils.download(url,
                sApplication.getString(R.string.app_name) + ".apk",
                null);
        return downloadId;
    }


    private static Uri getDownloadedApk() {
        long downloadId = getDownloadID();
        if (downloadId > 0) {
            return DownloadUtils.getUriByDownloadId(downloadId);
        }
        return null;
    }

    private static String getDownloadedApkPath() {
        Uri apkUri = getDownloadedApk();
        if (apkUri != null) {
            return new File(apkUri.getPath()).toString();
        }
        return null;
    }

    private static long getDownloadID() {
        return sApplication.getSP().getLong(SP_KEY_DOWNLOAD_ID, -1);
    }

    private static PackageInfo getApkInfo(String path) {
        PackageManager pm = sApplication.getPackageManager();
        return pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
    }

    class UpgradeInfo {
        @SerializedName(value = "app_name", alternate = {"appName"})
        private String appName;
        @SerializedName(value = "app_version", alternate = {"appVersion"})
        private String appVersion;
        private String description;
        private String url;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public enum UpgradeStatus {
        NEWEST,
        NO_METADATA,
        DOWNLOADING,
        INSTALL_FROM_LOCAL,
        REQUEST_ERROR,
    }
}
