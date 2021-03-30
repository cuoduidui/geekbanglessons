package com.cdd.messaging.reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 业务数据订阅者
 *
 * @param <T>
 */
public class BusinessSubscriber<T> implements Subscriber<T> {

    private Subscription subscription;

    private int count = 0;

    private final long maxRequest;

    public BusinessSubscriber(long maxRequest) {
        this.maxRequest = maxRequest;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        this.subscription.request(maxRequest);
    }

    @Override
    public void onNext(Object o) {
        System.out.println("收到数据：" + o);
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("遇到异常：" + t);
    }

    @Override
    public void onComplete() {
        System.out.println("收到数据完成");
    }
}
