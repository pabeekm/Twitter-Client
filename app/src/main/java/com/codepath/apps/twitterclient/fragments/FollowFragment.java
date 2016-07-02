package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.FollowArrayAdapter;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by pbeekman on 6/29/16.
 */
public class FollowFragment extends DialogFragment {
    @BindView(R.id.lvUsers) ListView lvUsers;
    @BindView(R.id.exit) ImageView exit;
    FollowArrayAdapter adapter;
    TwitterClient client;
    boolean type;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow_list, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getDialog().setTitle(type ? "Followers" : "Following");
            ButterKnife.bind(this, view);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                dismiss();
                }
            });
             // Load the list of users
             JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        adapter = new FollowArrayAdapter(getActivity(),
                                User.fromJSONArray(response.getJSONArray("users")));
                        lvUsers.setAdapter(adapter);
                    } catch (JSONException e) {
                    }
                }
            };
            if (type)
                client.getFollowers(handler, user);
            else
                client.getFollowing(handler, user);
        } catch (NullPointerException e) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            return;
        }
    }

    public static FollowFragment newInstance(boolean type, User user) {
        FollowFragment frag = new FollowFragment();
        frag.client = TwitterApplication.getRestClient();
        frag.type = type;
        frag.user = user;
        return frag;
    }
}
