package com.example.buriedlove.mysqlitehelper.Sql;

import android.provider.BaseColumns;

/**
 * Created by Timmy on 2016/9/27.
 */
public class FeedReaderContract {
    public FeedReaderContract() {}

    public static abstract class UserInfo implements BaseColumns {
        public static final String TABLE_NAME          = "user_info";
        public static final String COLUMN_APP_USER_ID = "app_user_id";
        public static final String COLUMN_NICKNAME   = "nickname";
        public static final String COLUMN_POST        = "post";
    }
}
