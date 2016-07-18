package com.news.home.managers;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.infra.managers.DataManager;
import com.infra.managers.DataObserver;
import com.infra.managers.home.models.Feed;
import com.infra.managers.home.models.NewsItem;
import com.infra.managers.requests.RetryWithDelay;
import com.infra.managers.responses.ServiceResponse;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created by hetashah on 7/17/16.
 */
public class FeedManager extends DataManager<Feed> {
    protected Subscription subscribe;
    private long lastId;

    @Override
    protected Observable<ServiceResponse<Feed>> getRxObservable() {
        final Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
        return Observable.interval(API_INTERVAL, TimeUnit.SECONDS, scheduler)
                .startWith(0L) // For an immediate 1st time
                .map(response -> {
                    try {
                        return filterResult(unpackResult(getServiceImpl().getLatestNews()));
                    } catch (Throwable t) {
                        throw Exceptions.propagate(t);
                    }
                })
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(API_RETRY, API_RETRY_START_MILLISECOND));
    }

    private ServiceResponse<Feed> filterResult(ServiceResponse<Feed> feedServiceResponse) {
        if (feedServiceResponse != null) {
            Feed feed = feedServiceResponse.getData();
            if (feed != null) {
                List<NewsItem> newsItems = feed.getData();
                if (newsItems != null && !newsItems.isEmpty()) {
                    NewsItem firstItem = null;
                    if (lastId == 0) {
                        firstItem = newsItems.get(0);
                    } else {
                        List<NewsItem> subList = Stream.of(newsItems)
                                .filter((NewsItem newsItem) -> newsItem != null &&
                                        lastId < newsItem.getId())
                                .collect(Collectors.toList());
                        if (subList != null &&
                                !subList.isEmpty()) {
                            firstItem = subList.get(0);
                        }
                        feed.setData(subList);
                    }

                    if (firstItem != null) {
                        lastId = firstItem.getId();
                    }
                }
            }
        }
        return feedServiceResponse;
    }

    public void getNews() {
        if (subscribe != null) {
            stopNews();
        }

        Observable<ServiceResponse<Feed>> observable = getRxObservable();
        subscribe = observable.subscribe((ServiceResponse<Feed> feedServiceResponse) -> {
            DataObserver<Feed> observer = getObserver();
            observer.notifyDataChanged(feedServiceResponse);
        });
    }

    public void stopNews() {
        if (subscribe != null &&
                !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        subscribe = null;
    }
}
