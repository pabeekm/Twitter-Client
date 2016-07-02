package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.TweetArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbeekman on 6/28/16.
 */
public abstract class TweetsListFragment extends Fragment {
    @BindView(R.id.lvTweets) ListView lvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    TweetArrayAdapter adapter;
    TwitterClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet_list, parent, false);
        ButterKnife.bind(this, v);
        lvTweets.setAdapter(adapter);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                populateTimeline();
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        return v;
    }

    public abstract void populateTimeline();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        client = TwitterApplication.getRestClient();
        adapter = new TweetArrayAdapter(getActivity(), new ArrayList<Tweet>());
        super.onCreate(savedInstanceState);
        populateTimeline();
    }

    public void addAll(List<Tweet> tweets) {
        adapter.addAll(tweets);
    }
}
