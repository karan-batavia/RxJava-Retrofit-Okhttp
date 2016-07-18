package com.news.home.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    /**
     * Adding count limit of news. In Future, this limit should come from server side configuration
     */
    private static final int NEWS_FEED_LIMIT = 100;

    @Bind(R.id.rvNewsFeed)
    RecyclerView rvNewsFeed;

    NewsListAdapter adapter;
    List<NewsItem> newsFeed = new ArrayList<>();
    FeedManager manager;

    boolean fetchNews = true;

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
        if(fetchNews) {
            startSubscription();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fetchNews) {
            stopSubscription();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feed,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.muSync:
                if(item.isChecked()) {
                    item.setChecked(false);
                    fetchNews = false;
                    stopSubscription();
                    updateMenuItemIcon(item, R.drawable.ic_sync_disable_24dp);
                } else {
                    item.setChecked(true);
                    fetchNews = true;
                    startSubscription();
                    updateMenuItemIcon(item, R.drawable.ic_sync_active_24dp);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stopSubscription() {
        if(manager != null) {
            manager.stopNews();
        }
    }

    private void startSubscription() {
        if(manager != null) {
            manager.getNews();
        }
    }

    private void updateMenuItemIcon(MenuItem item, int drawableId) {
        if(item != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                item.setIcon(ContextCompat.getDrawable(this, drawableId));
            } else {
                item.setIcon(getResources().getDrawable(drawableId));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        manager = null;
        adapter = null;
        if(newsFeed != null) {
            newsFeed.clear();
            newsFeed = null;
        }
    }

    @Override
    public void notifyDataChanged(ServiceResponse<Feed> collection) {
        if(collection != null) {
            Feed feed = collection.getData();
            if(feed != null) {
                List<NewsItem> newsItems = feed.getData();
                if(newsItems != null && !newsItems.isEmpty()) {
                    if(newsFeed.isEmpty()) {
                        if(newsItems.size() > NEWS_FEED_LIMIT) {
                            newsFeed.addAll(newsItems.subList(0, NEWS_FEED_LIMIT));
                        } else {
                            newsFeed.addAll(newsItems);
                        }
                    } else {
                        for(int i=newsItems.size() - 1; i >= 0; i--) {
                            NewsItem item = newsItems.get(i);
                            if(item != null) {
                                /**
                                 * Adding animation to only newly fetched items.
                                 */
                                item.setAnimate(true);
                                newsFeed.add(0, item);

                                if(newsFeed.size() > NEWS_FEED_LIMIT) {
                                    newsFeed.remove(newsFeed.size() - 1);
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    rvNewsFeed.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollToIndex(0);
                        }
                    });
                }
            }
        }
    }

    private void scrollToIndex(int position) {
        if(position < adapter.getItemCount()) {
            // Scroll to specified position
            rvNewsFeed.smoothScrollToPosition(position);
        }
    }
}
