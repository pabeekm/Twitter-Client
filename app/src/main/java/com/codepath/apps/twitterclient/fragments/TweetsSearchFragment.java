package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.TweetArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by pbeekman on 7/1/16.
 */
public class TweetsSearchFragment extends DialogFragment {
    @BindView(R.id.lvTweets) ListView lvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.exit)ImageView exit;
    TweetArrayAdapter adapter;
    TwitterClient client;
    String query;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_search_tweets, parent, false);
        ButterKnife.bind(this, v);
        getDialog().setTitle("Search results for \"" + query + "\"");
        populateTimeline();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        return v;
    }

    public void populateTimeline() {
        client.searchTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    adapter = new TweetArrayAdapter(getContext(), Tweet.fromJSONArray(response.getJSONArray("statuses")));
                } catch (JSONException e) {}
                lvTweets.setAdapter(adapter);
            }
        }, query);
    }

    public static TweetsSearchFragment newInstance(String query)  {
        TweetsSearchFragment fragmentDemo = new TweetsSearchFragment();
        fragmentDemo.query = query;
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        client = TwitterApplication.getRestClient();
        super.onCreate(savedInstanceState);
    }
}
