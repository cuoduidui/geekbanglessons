package com.cdd.messaging.reactive.streams;

import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author yangfengshan
 * @create 2021-03-31 18:52
 **/
public class SimpleProcessor implements Processor {
    private final Subscriber subscriber;
    private final Publisher publisher;

    public SimpleProcessor(Subscriber subscriber, Publisher publisher) {
        this.subscriber = subscriber;
        this.publisher = publisher;
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        publisher.subscribe(subscriber);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(Object o) {
        subscriber.onNext(o);
    }

    @Override
    public void onError(Throwable throwable) {
        subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }
}
