package com.codepath.apps.twitterclient.fragments;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pbeekman on 6/28/16.
 */
public class HomeTimelineFragment extends TweetsListFragment{

    @Override
    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                adapter.clear();
                addAll(Tweet.fromJSONArray(response));
            }
        });
    }
}
