package io.keepcoding.twlocator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.keepcoding.twlocator.R;
import io.keepcoding.twlocator.model.Tweet;
import io.keepcoding.twlocator.model.dao.TweetDao;

/**
 * Created by arodriguez on 11/7/15.
 */
public class DataListAdapter extends CursorAdapter {

    @Bind(R.id.text_tweet)
    TextView txtTweet;

    @Bind(R.id.imageView)
    ImageView imgTweet;


    private Cursor dataCursor;


    private LayoutInflater layoutInflater;


    public DataListAdapter(Context context, Cursor cursor){
        super(context, cursor);


        this.layoutInflater = LayoutInflater.from(context);
        this.dataCursor = cursor;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //Acá se crea la vista
        View view = layoutInflater.inflate(R.layout.view_tweet, viewGroup, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Tweet tweet = TweetDao.tweetFromCursor(cursor);
        txtTweet.setText(tweet.getText());
        Picasso.with(context).load(tweet.getImageURL()).into(imgTweet);

    }
}
