package com.codepath.apps.twitterclient.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pbeekman on 6/29/16.
 */
public class FollowArrayAdapter extends ArrayAdapter<User> {
    @BindView(R.id.ivPic) ImageView pic;
    @BindView(R.id.tvName) TextView title;

    public FollowArrayAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_1, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.follow_item,
                    parent, false);
        ButterKnife.bind(this, convertView);
        title.setText(user.getName());
        pic.setImageResource(0);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(pic);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(user));
                getContext().startActivity(i);
            }
        });
        return convertView;
    }
}
