package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pbeekman on 6/28/16.
 */
public class UserTimelineFragment extends TweetsListFragment {
    String screenName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline();
    }

    @Override
    public void populateTimeline() {
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
            }
        });
    }

    public static UserTimelineFragment newInstance(String screenName)  {
        UserTimelineFragment frag = new UserTimelineFragment();
        frag.screenName = screenName;
        return frag;
    }

}
