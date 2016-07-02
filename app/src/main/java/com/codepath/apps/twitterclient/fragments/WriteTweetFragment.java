package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.TimelineActivity;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by pbeekman on 6/28/16.
 */
public class WriteTweetFragment extends DialogFragment {
    @BindView(R.id.etTweet) EditText etTweet;
    @BindView(R.id.btnCancel) Button btnCancel;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    TwitterClient client;
    String title;
    String tweetText;
    TimelineActivity parent;

    public void setParent(TimelineActivity t) {
        parent = t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose_tweet, container);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getDialog().setTitle(title);
        // Configure submit and cancel buttons
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTweet.setText("");
                dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetText =etTweet.getText().toString();
                client.postTweet(new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        parent.fetchTimelineAsync();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    }
                }, tweetText);
                etTweet.setText("");
                dismiss();
            }
        });

        // Configure character count
        final TextView tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
        tvCharCount.setText("140 characters remaining");
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharCount.setText(String.valueOf(140 - s.length()) + " characters remaining");
            }

            public void afterTextChanged(Editable s) {
            }
        };
        etTweet.addTextChangedListener(mTextEditorWatcher);


    }
    public static WriteTweetFragment newInstance(String title) {
        WriteTweetFragment frag = new WriteTweetFragment();
        frag.client = TwitterApplication.getRestClient();
        frag.title = title;
        return frag;
    }
}
