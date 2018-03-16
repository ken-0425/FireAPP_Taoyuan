package library;

import android.provider.BaseColumns;

/**
 * Created by HSS-sysb1 on 2018/1/16.
 */

public final class SqliteInfo {


    public SqliteInfo() {}

    public static final class bloodsugar implements BaseColumns {


        public static final String TABLE_NAME = "bloodsugar";
        public static final String KEY_ID="_id";
        public static final String KEY_VALUE = "value";
        public static final String KEY_INFO = "info";
        public static final String KEY_CREATED_AT = "created_at";
    }
}


