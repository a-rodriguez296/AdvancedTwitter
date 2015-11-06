package io.keepcoding.twlocator.services;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import io.keepcoding.twlocator.util.twitter.TwitterHelper;
import twitter4j.AsyncTwitter;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;

public class TwitterServices {


    private static TwitterServices sInstance;

    public WeakReference<TweetsListener> mTweetsListener;

    private Context mContext;

    private AsyncTwitter mTwitter;


    public static TwitterServices getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TwitterServices(context);
        }
        return sInstance;
    }

    public TwitterServices(Context context) {
        mContext = context;
        mTwitter = new TwitterHelper(mContext).getAsyncTwitter();
        mTweetsListener = new WeakReference<TweetsListener>((TweetsListener) context);
    }


    public void searchTweet(GeoLocation location) {

        TwitterListener listener = new TwitterAdapter() {

            @Override
            public void searched(QueryResult queryResult) {
                super.searched(queryResult);

                //Programación defensiva
                if (mTweetsListener != null) {

                    mTweetsListener.get().tweetsDidLoad(queryResult.getTweets());


                }
            }

            @Override
            public void onException(TwitterException te, TwitterMethod method) {
                super.onException(te, method);
                if (mTweetsListener != null) {
                    mTweetsListener.get().tweetsDidFailLoading();
                }
            }
        };

        mTwitter.addListener(listener);
        Query query = new Query();
        query.geoCode(location, 10, Query.KILOMETERS.name());
        query.setCount(20);
        mTwitter.search(query);
    }


    public interface TweetsListener {

        void tweetsDidLoad(List<Status> tweets);

        void tweetsDidFailLoading();

    }
}
