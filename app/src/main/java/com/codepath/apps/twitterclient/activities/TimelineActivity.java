package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.fragments.TweetsSearchFragment;
import com.codepath.apps.twitterclient.fragments.WriteTweetFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {
    @BindView(R.id.viewpager) ViewPager pager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    private WriteTweetFragment fragmentCompose;
    TwitterClient client;
    TweetsPagerAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();
        adapter = new TweetsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabStrip.setViewPager(pager);
    }

    public void fetchTimelineAsync() {
        adapter.home.populateTimeline();
        adapter.mentions.populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        fragmentCompose = WriteTweetFragment.newInstance("Write Tweet");
        fragmentCompose.setParent(this);
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TweetsSearchFragment fragmentTweets = TweetsSearchFragment.newInstance(query);
                FragmentManager fm = getSupportFragmentManager();
                fragmentTweets.show(fm, "fragment_settings");
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(i);
    }

    public void onCompose(MenuItem mi) {
        FragmentManager fm = getSupportFragmentManager();
        fragmentCompose.show(fm, "fragment_settings");
    }


    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        HomeTimelineFragment home;
        MentionsTimelineFragment mentions;
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (home == null)
                    home = new HomeTimelineFragment();
                return home;
            }
            else if (position == 1) {
                if (mentions == null)
                    mentions = new MentionsTimelineFragment();
                return mentions;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
