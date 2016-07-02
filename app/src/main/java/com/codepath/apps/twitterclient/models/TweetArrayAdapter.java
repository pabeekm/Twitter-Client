package com.codepath.apps.twitterclient.models;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.fragments.TweetFragment;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbeekman on 6/27/16.
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet>{
    @BindView(R.id.ivPic) ImageView pic;
    @BindView(R.id.ivEmbedded) ImageView embedded;
    @BindView(R.id.tvTime) TextView time;
    @BindView(R.id.tvBody) TextView body;
    @BindView(R.id.tvName) TextView title;


    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item,
                    parent, false);
        ButterKnife.bind(this, convertView);
        title.setText(tweet.getUser().getName());
        time.setText(tweet.getCreatedAt());
        body.setText(tweet.getBody());
        pic.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(pic);
        embedded.setImageResource(0);
        Picasso.with(getContext()).load(tweet.getImageUrl()).into(embedded);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                TweetFragment.newInstance(tweet).show(fm, tweet.getUser().getName());
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(tweet.getUser()));
                getContext().startActivity(i);
            }
        };
        pic.setOnClickListener(listener);
        title.setOnClickListener(listener);
        return convertView;
    }
}
