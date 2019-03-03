package net.bndy.ad.framework.data;

import java.util.HashMap;
import java.util.Map;

public class SQLiteTypeMapping {
    private static final Map<String, String> DIC = new HashMap(){{
        put("java.lang.Integer", "INTEGER");
        put("java.lang.String", "TEXT");
        put("java.lang.Boolean", "TEXT");
        put("java.lang.Byte", "INTEGER");
        put("java.lang.Double", "REAL");
        put("java.lang.Float", "REAL");
        put("java.lang.Long", "REAL");
    }};

    public static String toSQLiteType(Class<?> type) {
        return DIC.get(type.getSimpleName());
    }
}
