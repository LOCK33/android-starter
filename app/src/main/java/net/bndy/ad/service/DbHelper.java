package net.bndy.ad.service;

import android.content.Context;

import net.bndy.ad.framework.data.SQLiteForEntityProvider;

public class DbHelper extends SQLiteForEntityProvider {

    private static final String DB_NAME = "LOG";

    public DbHelper(Context context, Class<?>... clazzes) {
        super(context, DB_NAME, clazzes);
    }
}
