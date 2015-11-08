package io.keepcoding.twlocator.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.keepcoding.twlocator.R;
import io.keepcoding.twlocator.fragment.TweetListFragment;
import io.keepcoding.twlocator.model.dao.TweetDao;
import io.keepcoding.twlocator.provider.TwLocatorProvider;

public class TweetsListsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TweetListFragment tweetsFragment;


    private final LoaderManager loaderManager = getLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_lists);

        tweetsFragment = (TweetListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);


        loaderManager.initLoader(1, null,//Bundle parámetro
                this); //Delegado

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = new CursorLoader(this, TwLocatorProvider.TWEETS_URI, TweetDao.allColumns, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        tweetsFragment.setCursor(cursor);

        tweetsFragment.setIdLayout(R.layout.fragment_tweet_list);
        tweetsFragment.setIdListView(R.id.list_view);

        tweetsFragment.refreshData();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
