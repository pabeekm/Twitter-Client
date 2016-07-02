package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbeekman on 6/30/16.
 */
public class TweetFragment extends DialogFragment {
    @BindView(R.id.ivPic) ImageView pic;
    @BindView(R.id.ivEmbedded) ImageView embedded;
    @BindView(R.id.tvTime) TextView time;
    @BindView(R.id.tvBody) TextView body;
    @BindView(R.id.exit)ImageView exit;
    TwitterClient client;
    Tweet tweet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet, container);
        ButterKnife.bind(this, v);
        try {
            getDialog().setTitle(tweet.getUser().getName());
        } catch (NullPointerException e) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            return v;
        }
        time.setText(tweet.getCreatedAt());
        body.setText(tweet.getBody());
        pic.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(pic);
        embedded.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getImageUrl()).into(embedded);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(tweet.getUser()));
                getContext().startActivity(i);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;

    }

    public static TweetFragment newInstance(Tweet tweet) {
        TweetFragment frag = new TweetFragment();
        frag.client = TwitterApplication.getRestClient();
        frag.tweet = tweet;
        return frag;
    }

}
