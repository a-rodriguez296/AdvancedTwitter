package io.keepcoding.twlocator.model.db;

public class DBConstants {

    public static final String DROP_DATABASE = "";

    public static final String TABLE_TWEET = "TWEET";


    public static final String KEY_TWEET_ID = "_id";

    public static final String KEY_TWEET_TEXT = "text";
    public static final String KEY_TWEET_CREATION_DATE = "creationDate";
    public static final String KEY_TWEET_MODIFICATION_DATE = "modificationDate";
    public static final String KEY_TWEET_LATITUDE = "latitude";
    public static final String KEY_TWEET_LONGITUDE = "longitude";

    public static final String SQL_CREATE_TWEET_TABLE =
            "create table "
                    + TABLE_TWEET + " ( " + KEY_TWEET_ID
                    + " integer primary key autoincrement, "
                    + KEY_TWEET_TEXT + " text not null, "
                    + KEY_TWEET_CREATION_DATE + " INTEGER, "
                    + KEY_TWEET_MODIFICATION_DATE + " INTEGER, "
                    + KEY_TWEET_LONGITUDE + " real, "
                    + KEY_TWEET_LATITUDE + " real "
                    + ");";


    public static final String[] CREATE_DATABASE = {
            SQL_CREATE_TWEET_TABLE
    };


}
