package net.bndy.ad.framework.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class SQLiteProvider extends SQLiteOpenHelper {

    private static final String TAG = SQLiteProvider.class.getSimpleName();

    private String sqlCreate;
    private String sqlUpdate;

    public SQLiteProvider(Context context, String dbName, int dbVersion, String sqlCreate) {
        this(context, dbName, dbVersion, sqlCreate, null);
    }

    public SQLiteProvider(Context context, String dbName, int dbVersion, String sqlCreate, String sqlUpdate) {
        super(context, dbName, null, dbVersion);
        this.sqlCreate = sqlCreate;
        this.sqlUpdate = sqlUpdate;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (this.sqlCreate == null || this.sqlCreate == "") {
            Log.w(TAG, "No sql provided to create database");
        } else {
            db.execSQL(this.sqlCreate);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (this.sqlUpdate != null) {
            db.execSQL(this.sqlUpdate);
        }
        onCreate(db);
    }
}
