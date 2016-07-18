package com.news.home.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.infra.managers.DataObserver;
import com.infra.managers.home.models.Feed;
import com.infra.managers.home.models.NewsItem;
import com.infra.managers.responses.ServiceResponse;
import com.news.R;
import com.news.home.adapters.NewsListAdapter;
import com.news.home.managers.FeedManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedActivity extends AppCompatActivity implements DataObserver<Feed>{
    private static final int NEWS_FEED_LIMIT = 100;

    @Bind(R.id.rvNewsFeed)
    RecyclerView rvNewsFeed;

    NewsListAdapter adapter;
    List<NewsItem> newsFeed = new ArrayList<>();
    FeedManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new NewsListAdapter(newsFeed);
        rvNewsFeed.setAdapter(adapter);
        rvNewsFeed.setLayoutManager(new LinearLayoutManager(this));
        rvNewsFeed.setHasFixedSize(true);

        manager = new FeedManager();
        manager.setObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(manager != null) {
            manager.getNews();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(manager != null) {
            manager.stopNews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        manager = null;
        adapter = null;
    }

    @Override
    public void notifyDataChanged(ServiceResponse<Feed> collection) {
        if(collection != null) {
            Feed feed = collection.getData();
            if(feed != null) {
                List<NewsItem> newsItems = feed.getData();
                if(newsItems != null && !newsItems.isEmpty()) {
                    if(newsFeed == null) {
                        if(newsItems.size() > NEWS_FEED_LIMIT) {
                            newsFeed.addAll(newsItems.subList(0, NEWS_FEED_LIMIT));
                        } else {
                            newsFeed.addAll(newsItems);
                        }
                    } else {
                        for(int i=newsItems.size() - 1; i >= 0; i--) {
                            NewsItem item = newsItems.get(i);
                            if(item != null) {
                                newsFeed.add(0, item);

                                if(newsFeed.size() > NEWS_FEED_LIMIT) {
                                    newsFeed.remove(newsFeed.size() - 1);
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
