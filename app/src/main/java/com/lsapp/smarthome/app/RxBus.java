package com.lsapp.smarthome.app;

/**
 * Created by Administrator on 2016/3/30.
 */

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus
 * Created by YoKeyword on 2015/6/17.
 */
public class RxBus {
    private static volatile RxBus defaultInstance;
    private final Subject bus;

    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return bus.ofType(eventType);

    }
}