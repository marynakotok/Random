package com.example.marina.random;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static  final  int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "use.db";
    public static final String TABLE_CONTACTS = "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "_name";
    public static final String KEY_GENDER = "_gender";
    public static final String KEY_REGISTERED = "_registed";
    public static final String KEY_USERNAME = "_username";
    public static final String KEY_PASSWORD = "_password";
    public static final String KEY_NAT = "_nat";
    public static final String KEY_LOCALIZATION = "_localization";
    public static final String KEY_PICTURE = "_picture";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //if the db doesn't exist , we will create it
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql ="CREATE TABLE " +   TABLE_CONTACTS + " ( " +
                KEY_ID +" integer primary key autoincrement, " +
                KEY_NAME + " text not null, " +
                KEY_GENDER + " text not null, " +
                KEY_LOCALIZATION + " text not null, " +
                KEY_NAT + " text not null, " +
                KEY_PASSWORD + " text not null, " +
                KEY_USERNAME + " text not null, " +
                KEY_REGISTERED + " text not null, " +
                KEY_PICTURE + " text not null " + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
}
