package com.codepath.apps.twitterclient.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by pbeekman on 6/27/16.
 */
public class Tweet {
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    private String imageUrl;

    public String getImageUrl()
    {
        return imageUrl;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        Calendar c = new GregorianCalendar();
        // give postTime a default value
        Date postTime = c.getTime();
        // set postTime to the timeStamp time
        DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        try {
            postTime = format.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = c.getTime();
        String str = DateUtils.getRelativeTimeSpanString(postTime.getTime(), now.getTime(),
                DateUtils.MINUTE_IN_MILLIS).toString();
        str = str.replace(" minutes ago", "m");
        str = str.replace(" hours ago", "h");
        str = str.replace(" days ago", "d");
        str = str.replace(" years ago", "y");
        str = str.replace("in 0 minutes", "Just now");
        str = str.replace("0m", "Just now");
        return str;
    }

    public static Tweet fromJSON(JSONObject j) {
        Tweet t = new Tweet();
        try {
            t.body = j.getString("text");
            t.uid = j.getLong("id");
            t.createdAt = j.getString("created_at");
            t.user = User.fromJSON(j.getJSONObject("user"));
            JSONArray media = j.getJSONObject("entities").getJSONArray("media");
            if (media.length() > 0)
                t.imageUrl = ((JSONObject) media.get(0)).getString("media_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return t;

    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray j) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 0; i < j.length(); i++) {
            try {
                Tweet t = fromJSON(j.getJSONObject(i));
                if (t != null)
                    tweets.add(t);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
