package io.keepcoding.twlocator.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import static io.keepcoding.twlocator.model.db.DBConstants.*;

import java.util.Date;

import io.keepcoding.twlocator.model.Tweet;
import io.keepcoding.twlocator.model.db.DBHelper;

public class TweetDao {

    public static final String[] allColumns = {
            KEY_TWEET_ID,
            KEY_TWEET_TEXT,
            KEY_TWEET_CREATION_DATE,
            KEY_TWEET_MODIFICATION_DATE,
            KEY_TWEET_IMAGE_URL,
            KEY_TWEET_LONGITUDE,
            KEY_TWEET_LATITUDE,


    };

    @NonNull
    public static Tweet tweetFromCursor(Cursor cursor) {
        Tweet tweet = new Tweet(cursor.getString(cursor.getColumnIndex(KEY_TWEET_TEXT)),
                cursor.getDouble(cursor.getColumnIndex(KEY_TWEET_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(KEY_TWEET_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(KEY_TWEET_IMAGE_URL)));

        tweet.setId(cursor.getLong(cursor.getColumnIndex(KEY_TWEET_ID)));

        long creationDate = cursor.getLong(cursor.getColumnIndex(KEY_TWEET_CREATION_DATE));
        tweet.setCreationDate(DBHelper.convertLongToDate(creationDate));

        long modificationDate = cursor.getLong(cursor.getColumnIndex(KEY_TWEET_MODIFICATION_DATE));
        tweet.setModificationDate(DBHelper.convertLongToDate(modificationDate));

        return tweet;
    }

    public static ContentValues getContentValues(Tweet tweet) {


        if (tweet.getCreationDate() == null) {
            tweet.setCreationDate(new Date());
        }

        if (tweet.getModificationDate() == null) {
            tweet.setModificationDate(new Date());
        }


        ContentValues content = new ContentValues();
        content.put(KEY_TWEET_TEXT, tweet.getText());
        content.put(KEY_TWEET_CREATION_DATE, DBHelper.convertDateToLong(tweet.getCreationDate()));
        content.put(KEY_TWEET_MODIFICATION_DATE, DBHelper.convertDateToLong(tweet.getModificationDate()));
        content.put(KEY_TWEET_IMAGE_URL, tweet.getImageURL());
        content.put(KEY_TWEET_LATITUDE, String.format("%f", tweet.getLatitude()));
        content.put(KEY_TWEET_LONGITUDE, String.format("%f", tweet.getLongitude()));


        return content;
    }
    
}
