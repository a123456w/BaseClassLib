package com.android.qizx.baselibrary.http;

import rx.Subscription;

/*
 *  Created by 李  on 2018/10/24.
 */
interface RxActionManager<T> {
        void add(T tag, Subscription subscription);
        void remove(T... tag);
        void cancel(T tag);
        void cancelAll();
}
