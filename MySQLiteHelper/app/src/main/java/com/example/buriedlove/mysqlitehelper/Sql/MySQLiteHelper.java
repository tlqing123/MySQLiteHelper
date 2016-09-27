package com.example.buriedlove.mysqlitehelper.Sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;


/**
 * Created by Timmy on 2016/9/27.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME  = "TimmyDB";

    private static final String UNIQUE    = "UNIQUE";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE  = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES_USER_INFO = "CREATE TABLE "
                    + FeedReaderContract.UserInfo.TABLE_NAME + "("
                    + FeedReaderContract.UserInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FeedReaderContract.UserInfo.COLUMN_APP_USER_ID + INT_TYPE + COMMA_SEP
                    + FeedReaderContract.UserInfo.COLUMN_NICKNAME + TEXT_TYPE + COMMA_SEP
                    + FeedReaderContract.UserInfo.COLUMN_POST + TEXT_TYPE + ")";


    private static final String SQL_DELETE_ENTRIES_USER_INFO =
            "DROP TABLE IF EXISTS " + FeedReaderContract.UserInfo.TABLE_NAME;

    private Context mContext;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_USER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_USER_INFO);
        onCreate(db);
    }

    public boolean haveUser(String app_user_id) {
        String deValue = "";
        if(TextUtils.isEmpty(getUserInfoDB(app_user_id , new String[]{FeedReaderContract.UserInfo.COLUMN_APP_USER_ID},deValue))){
            return false;
        }else {
            return true;
        }
    }

    public String getPost(String app_user_id) {
        String deValue = "";
        return getUserInfoDB(app_user_id , new String[]{FeedReaderContract.UserInfo.COLUMN_POST} , deValue);
    }

    public String getNickname(String app_user_id , String realName) {
        return getUserInfoDB(app_user_id , new String[]{FeedReaderContract.UserInfo.COLUMN_NICKNAME} , realName);
    }

    public String getUserInfoDB(String app_user_id , String[] column , String deValue) {
        SQLiteDatabase dbReadable = this.getReadableDatabase();
        Cursor c = dbReadable.query(FeedReaderContract.UserInfo.TABLE_NAME,
                column,
                FeedReaderContract.UserInfo.COLUMN_APP_USER_ID + " = ?",
                new String[]{app_user_id},
                null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        if (c == null || c.getCount() <= 0 || !c.moveToFirst()) {
            return deValue;
        }
        try {
            deValue = c.getString(0);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            c.close();
            dbReadable.close();
        }
        return deValue;

    }

    public void updateNickname(String app_user_id, String nickname) {
        updateUserInfo(app_user_id, nickname, null);
    }

    public void updatePost(String app_user_id, String post) {
        updateUserInfo(app_user_id, null, post);
    }

    public void updateUserInfo(String app_user_id, String nickname, String post) {
        ContentValues cv = new ContentValues();
        if (app_user_id != null) {
            cv.put(FeedReaderContract.UserInfo.COLUMN_APP_USER_ID, app_user_id);
        }
        if (nickname != null) {
            cv.put(FeedReaderContract.UserInfo.COLUMN_NICKNAME, nickname);
        }
        if (post != null) {
            cv.put(FeedReaderContract.UserInfo.COLUMN_POST, post);
        }
        updateUserInfoDB(cv);
    }

    public void updateUserInfoDB(ContentValues cv) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        try {
            int i = dbWrite.update(FeedReaderContract.UserInfo.TABLE_NAME, cv, FeedReaderContract.UserInfo.COLUMN_APP_USER_ID + " = " + cv.getAsString(FeedReaderContract.UserInfo.COLUMN_APP_USER_ID), null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbWrite.close();
        }
    }

    public void setNickname(String app_user_id, String nickname) {
        if(haveUser(app_user_id)){
            updateNickname(app_user_id , nickname);
        }else {
            setUserInfo(app_user_id, nickname, null);
        }
    }

    public void setPost(String app_user_id, String post) {
        if(haveUser(app_user_id)){
            updatePost(app_user_id , post);
        }else {
            setUserInfo(app_user_id, null, post);
        }
    }

    public void setUserInfo(String app_user_id, String nickname, String post) {
        ContentValues cv = new ContentValues();
        if (app_user_id != null) {
            cv.put(FeedReaderContract.UserInfo.COLUMN_APP_USER_ID, app_user_id);
        }
        if (nickname != null) {
            cv.put(FeedReaderContract.UserInfo.COLUMN_NICKNAME, nickname);
        }
        if (post != null) {
            cv.put(FeedReaderContract.UserInfo.COLUMN_POST, post);
        }
        setUserInfoDB(cv);
    }

    public void setUserInfoDB(ContentValues cv) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        try {
            dbWrite.insert(FeedReaderContract.UserInfo.TABLE_NAME, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbWrite.close();
        }

    }

    public void deleteUserInfoDB(String app_user_id) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        try {
            dbWrite.delete(FeedReaderContract.UserInfo.TABLE_NAME, FeedReaderContract.UserInfo.COLUMN_APP_USER_ID + "=" + app_user_id, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbWrite.close();
        }
    }
}
