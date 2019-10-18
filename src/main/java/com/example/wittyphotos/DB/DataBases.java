package com.example.wittyphotos.DB;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns{

        public static final String IMAGEID = "imageID";
        public static final String IMAGEPATH = "imagePath";
        public static final String IMAGETIME = "imageTime";
        public static final String GPS = "gps";
        public static final String LOCATION = "location";

        public static final String _TABLENAME0 = "imageInfo";
        public static final String _CREATE0 = "create table if not exists " +
                _TABLENAME0 + "(" +
                _ID + " integer primary key autoincrement, " +
                IMAGEID + " integer not null , " +
                IMAGEPATH + " text not null , " +
                IMAGETIME + " text , " +
                GPS + " text , " +
                LOCATION + " text ); ";

        public static final String TAGID = "tagID";
        public static final String _TABLENAME1 = "tagLog";
        public static final String _CREATE1 = "create table if not exists " +
                _TABLENAME1 + "(" +
                _ID + " integer primary key autoincrement, " +
                IMAGEID + " integer not null , " +
                TAGID + " integer not null ); ";

        public static final String TAG = "tag";
        public static final String CATEGORY = "category";
        public static final String _TABLENAME2 = "tagInfo";
        public static final String _CREATE2 = "create table if not exists " +
                _TABLENAME2 + "(" +
                _ID + " integer primary key autoincrement, " +
                TAG + " text not null , " +
                CATEGORY + " integer not null ); ";
    }
}
