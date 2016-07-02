package com.codepath.apps.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by pbeekman on 6/27/16.
 */
@Parcel
public class User {
    private String name;
    private long uid;
    private String profileImageUrl;
    private String screenName;
    private String tagline;
    private int followersCount;
    private int friendsCount;

    // required for Parceler
    public User() {
    }

    public static User fromJSON(JSONObject j) {
        User u = new User();
        try {
            u.name = j.getString("name");
            u.uid = j.getLong("id");
            u.screenName = j.getString("screen_name");
            u.profileImageUrl = j.getString("profile_image_url");
            u.tagline = j.getString("description");
            u.followersCount = j.getInt("followers_count");
            u.friendsCount = j.getInt("friends_count");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public static ArrayList<User> fromJSONArray(JSONArray j) {
        ArrayList<User> results = new ArrayList<>();
        for (int i = 0; i < j.length(); i++) {
            try {
                results.add(User.fromJSON(j.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public int getFollowersCount() {
        return followersCount;
    }
    public int getFriendsCount() {
        return friendsCount;
    }
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }
    public String getTagline() {
        return tagline;
    }
}
