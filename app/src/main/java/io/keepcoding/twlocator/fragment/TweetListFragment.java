package io.keepcoding.twlocator.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import io.keepcoding.twlocator.R;
import io.keepcoding.twlocator.adapter.DataListAdapter;

/**
 * Created by arodriguez on 11/7/15.
 */
public class TweetListFragment extends Fragment{

    private ListView listView;
    private Cursor cursor;
    private ListAdapter adapter;

    private int idLayout = R.layout.fragment_tweet_list;
    private int idListView;


    public static TweetListFragment getInstance(Cursor cursor, int idLayout, int idListView){
        TweetListFragment fragment = new TweetListFragment();

        fragment.cursor = cursor;
        fragment.idLayout = idLayout;
        fragment.idListView = idListView;

        return fragment;
    }

    public TweetListFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(idLayout,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listView = (ListView) getActivity().findViewById(idListView);

        if (listView != null){
//            refreshData()
        }
    }


    public void refreshData(){

        if (cursor == null){
            return;
        }

        listView = (ListView) getActivity().findViewById(idListView);

        adapter  = new DataListAdapter(getActivity(), cursor);
        listView.setAdapter(adapter);

    }


    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public int getIdLayout() {
        return idLayout;
    }

    public void setIdLayout(int idLayout) {
        this.idLayout = idLayout;
    }

    public int getIdListView() {
        return idListView;
    }

    public void setIdListView(int idListView) {
        this.idListView = idListView;
    }
}

