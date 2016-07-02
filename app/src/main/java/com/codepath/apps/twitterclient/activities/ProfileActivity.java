package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.fragments.FollowFragment;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTag)TextView tvTag;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivPic) ImageView ivPic;
    TwitterClient client;
    User user;
    FollowFragment followers;
    FollowFragment following;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();
        followers =  FollowFragment.newInstance(true, null);
        following =  FollowFragment.newInstance(false, null);
        String screenName = getIntent().getStringExtra("screen_name");
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        if (user == null) {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }
        });
        }
       else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }
            }, user);
            screenName = user.getScreenName();
            followers =  FollowFragment.newInstance(true, user);
            following =  FollowFragment.newInstance(false, user);
        }
        if (savedInstanceState == null) {
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rlContainter, fragment);
            ft.commit();
        }

    }
    public void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvTag.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");
        ivPic.setImageResource(0);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivPic);
    }
    public void onBack(MenuItem mi) {
        this.finish();
    }

    public void showFollowers(View v) {
        FragmentManager fm = getSupportFragmentManager();
        followers.show(fm, "fragment_settings");
    }
    public void showFollowing(View v) {
        FragmentManager fm = getSupportFragmentManager();
        following.show(fm, "fragment_settings");
    }
}
