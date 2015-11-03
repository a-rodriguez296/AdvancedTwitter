package io.keepcoding.twlocator.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import io.keepcoding.twlocator.TwLocatorApp;
import io.keepcoding.twlocator.model.Tweet;
import io.keepcoding.twlocator.model.dao.TweetDao;
import io.keepcoding.twlocator.model.db.DBHelper;

public class TwLocatorProviderHelper {

    public static String getIdFromUri(Uri uri) {
        String rowID = uri.getPathSegments().get(1);
        return rowID;
    }

    public static void deleteAllTweets(){
        ContentResolver cr = TwLocatorApp.getAppContext().getContentResolver();
        cr.delete(TwLocatorProvider.TWEETS_URI,null, null);

    }


    public static Cursor getAllTweets() {
        ContentResolver cr = TwLocatorApp.getAppContext().getContentResolver();
        Cursor cursor = cr.query(TwLocatorProvider.TWEETS_URI, TweetDao.allColumns, null, null, null);

        return cursor;
    }


    public static Uri insertTweet(Tweet tweet) {
        ContentResolver cr = TwLocatorApp.getAppContext().getContentResolver();
        if (tweet == null) {
            return null;
        }


        Uri uri = cr.insert(TwLocatorProvider.TWEETS_URI, TweetDao.getContentValues(tweet));
        tweet.setId(Long.parseLong(getIdFromUri(uri)));
        return uri;
    }

    public static void deleteTweet(Tweet tweet) {
        ContentResolver cr = TwLocatorApp.getAppContext().getContentResolver();
        String sUri = TwLocatorProvider.TWEETS_URI.toString() + "/" + tweet.getId();
        Uri uri = Uri.parse(sUri);
        cr.delete(uri, null, null);
    }

    public static int updateTweet(Tweet tweet) {
        if (tweet == null) {
            return (int) DBHelper.INVALID_ID;
        }

        ContentResolver cr = TwLocatorApp.getAppContext().getContentResolver();
        String sUri = TwLocatorProvider.TWEETS_URI.toString() + "/" + tweet.getId();
        Uri uri = Uri.parse(sUri);


        int updatedNotebooks = cr.update(uri, TweetDao.getContentValues(tweet), null, null);
        return updatedNotebooks;
    }
}
