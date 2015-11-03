package io.keepcoding.twlocator;

import android.test.AndroidTestCase;

import io.keepcoding.twlocator.model.Tweet;
import twitter4j.GeoLocation;

public class TweetTests extends AndroidTestCase{


    public void testCreateTweet(){


        Tweet tweet = new Tweet(10, "hello", new GeoLocation(3.45, 3.45));

        assertNotNull(tweet);

    }

}
