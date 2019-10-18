package com.example.wittyphotos.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper  {

    private static final String DBNAME = "wittyphotos.db";
    private static final int DBVERSION = 5;
    public Context mContext;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;

    private class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
            db.execSQL(DataBases.CreateDB._CREATE1);
            db.execSQL(DataBases.CreateDB._CREATE2);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME0);
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME1);
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME2);
        }
    }

    public DBOpenHelper(Context context){
        this.mContext = context;
    }

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mContext, DBNAME, null, DBVERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){ mDBHelper.onCreate(mDB); }
    public void close(){
        mDB.close();
    }

    //이미지 테이블
    public Cursor select_image(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }
    public long insert_image(long imageID, String imagePath, String imageTime, String gps, String location){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.IMAGEID, imageID);
        values.put(DataBases.CreateDB.IMAGEPATH, imagePath);
        values.put(DataBases.CreateDB.IMAGETIME, imageTime);
        values.put(DataBases.CreateDB.GPS, gps);
        values.put(DataBases.CreateDB.LOCATION, location);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }
    public boolean delete_image(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }

    //태그 로그 테이블
    public Cursor select_tagLog(){
        return mDB.query(DataBases.CreateDB._TABLENAME1, null, null, null, null, null, null);
    }
    public long insert_tagLog(long imageID, long tagID) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.IMAGEID, imageID);
        values.put(DataBases.CreateDB.TAGID, tagID);
        return mDB.insert(DataBases.CreateDB._TABLENAME1, null, values);
    }
    public boolean delete_tagLog(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME1, "_id="+id, null) > 0;
    }

    //태그 테이블
    public Cursor select_tag(){
        return mDB.query(DataBases.CreateDB._TABLENAME2, null, null, null, null, null, null);
    }
    public long insert_tag(String tag, int category) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.TAG, tag);
        values.put(DataBases.CreateDB.CATEGORY, category);
        return mDB.insert(DataBases.CreateDB._TABLENAME2, null, values);
    }
    public boolean delete_tag(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME2, "_id="+id, null) > 0;
    }
}
