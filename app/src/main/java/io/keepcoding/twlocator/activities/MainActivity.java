package io.keepcoding.twlocator.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.keepcoding.twlocator.R;
import io.keepcoding.twlocator.model.dao.TweetDao;
import io.keepcoding.twlocator.provider.TwLocatorProvider;
import io.keepcoding.twlocator.services.TwitterServices;
import io.keepcoding.twlocator.util.NetworkHelper;
import io.keepcoding.twlocator.util.twitter.ConnectTwitterTask;
import twitter4j.GeoLocation;
import twitter4j.Status;


public class MainActivity extends ActionBarActivity implements ConnectTwitterTask.OnConnectTwitterListener, LoaderManager.LoaderCallbacks<Cursor>, TwitterServices.TweetsListener {

    ConnectTwitterTask twitterTask;
    private static final int URL_LOADER = 0;

    MapFragment mapFragment;
    GoogleMap map;

    @Bind(R.id.editText)
    EditText mEditText;

    @Bind(R.id.button)
    Button button;
    private final LoaderManager loaderManager = getLoaderManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();


        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (NetworkHelper.isNetworkConnectionOK(new WeakReference<>(getApplication()))) {
            twitterTask = new ConnectTwitterTask(this);
            twitterTask.setListener(this);

            twitterTask.execute();
        } else {
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG).show();

        }

        final TwitterServices twitterServices = TwitterServices.getInstance(MainActivity.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeoLocation geoLocation = performSearch(mEditText.getText().toString());

                CameraUpdate center =
                        CameraUpdateFactory.newLatLng(new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);

                map.moveCamera(center);
                map.animateCamera(zoom);


                twitterServices.searchTweet(geoLocation);


//                Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(mapIntent);


            }
        });

        loaderManager.initLoader(0, null,//Bundle parámetro
                this); //Delegado
    }

 /*   private void launchTwitter(GeoLocation geoLocation) {

        AsyncTwitter twitter = new TwitterHelper(this).getAsyncTwitter();

        TwitterListener listener = new TwitterAdapter(){

            @Override
            public void searched(QueryResult queryResult) {
                super.searched(queryResult);
                    for (Status s: queryResult.getTweets()) {
                        Log.d("", s.getText() + " " + s.getGeoLocation());



                    }


            }

            @Override
            public void onException(TwitterException te, TwitterMethod method) {
                super.onException(te, method);
            }
        };

        twitter.addListener(listener);

        Query query = new Query();

        query.geoCode(geoLocation, 10, Query.KILOMETERS.name());
        query.setCount(20);
        twitter.search(query);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.options_menu_main_search).getActionView();
//        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        startSearch(null, false, appData, false);
        return true;
    }


    private GeoLocation performSearch(String searchString) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        GeoLocation geoLocation = null;
        try {
            addresses = geocoder.getFromLocationName(searchString, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                geoLocation = new GeoLocation(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            return geoLocation;
        }
    }

    @Override
    public void twitterConnectionFinished() {
        Toast.makeText(this, getString(R.string.twiiter_auth_ok), Toast.LENGTH_SHORT).show();


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Esto lo que hace es crear un hilo en 2do plano y pedirle al contentResolver q haga la consulta que le estoy pasando

        CursorLoader loader = new CursorLoader(this, TwLocatorProvider.TWEETS_URI, TweetDao.allColumns, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("Twitter", "Entro al loadFinish");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void tweetsDidLoad(final List<Status> tweets) {


        this.runOnUiThread(new Runnable() {
            public void run() {
                for (Status s : tweets) {

                    if (s.getGeoLocation() != null ){

//                        ImageView imageView = new ImageView(MainActivity.this);
//                        Picasso.with(MainActivity.this).load(s.getUser().getBiggerProfileImageURLHttps()).into(imageView);
//
//                        imageView.buildDrawingCache();
//                        Bitmap bmap = imageView.getDrawingCache();

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(s.getGeoLocation().getLatitude(), s.getGeoLocation().getLongitude()))
                                .title(s.getText()));


                    }

                }
            }
        });


    }

    public void tweetsDidFailLoading() {

    }


}

