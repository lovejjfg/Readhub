/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.HashMap

/**
 * Created by Joe on 2017/1/2.
 * Email lovejjfg@gmail.com
 */

class RxBus private constructor() {
    private val mSubject: Subject<Any> = PublishSubject.create<Any>().toSerialized()
    private var mSubscriptionMap: HashMap<String, CompositeDisposable> = HashMap()

    /**
     * 发送事件

     * @param o
     */
    fun post(o: Any) {
        mSubject.onNext(o)
    }

    /**
     * 返回指定类型的Observable实例

     * @param type
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> toObservable(type: Class<T>): Observable<T> {
        return mSubject.ofType(type)
    }

    val observable: Observable<Any>
        get() = mSubject

    /**
     * 是否已有观察者订阅

     * @return
     */
    fun hasObservers(): Boolean {
        return mSubject.hasObservers()
    }

    /**
     * 一个默认的订阅方法

     * @param type
     * *
     * @param next
     * *
     * @param error
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> doSubscribe(type: Class<T>, next: Consumer<T>, error: Consumer<Throwable>): Disposable {
        return toObservable(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next, error)
    }

    fun <T> addSubscription(o: Any, type: Class<T>, next: Consumer<T>, error: Consumer<Throwable>) {
        val disposable = toObservable(type)
            .subscribeOn(Schedulers.io())
            .distinct()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next, error)
        addSubscription(o, disposable)
    }

    /**
     * 保存订阅后的subscription

     * @param o
     * *
     * @param subscription
     */
    fun addSubscription(o: Any, subscription: Disposable) {
        val key = o.toString()
        val disposable = mSubscriptionMap[key]
        if (disposable != null) {
            disposable.add(subscription)
        } else {
            val compositeSubscription = CompositeDisposable()
            compositeSubscription.add(subscription)
            mSubscriptionMap[key] = compositeSubscription
        }
    }

    /**
     * 取消订阅

     * @param o
     */
    fun unSubscribe(o: Any) {

        val key = o.toString()
        if (!mSubscriptionMap.containsKey(key)) {
            return
        }
        mSubscriptionMap[key]?.dispose()

        mSubscriptionMap.remove(key)
    }

    companion object {
        @Volatile
        private var INSTANCE: RxBus? = null

        val instance: RxBus
            get() {
                if (INSTANCE == null) {
                    synchronized(RxBus::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = RxBus()
                        }
                    }
                }
                return INSTANCE ?: throw NullPointerException("init RxBus error")
            }
    }
}
