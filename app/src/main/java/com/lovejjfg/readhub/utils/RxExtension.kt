/*
 * Copyright (c) 2018.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.readhub.utils

import com.lovejjfg.readhub.base.ReadhubException
import com.lovejjfg.readhub.data.Cache
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by joe on 2018/11/8.
 * Email: lovejjfg@gmail.com
 */

inline fun <R> Observable<R>.ioToMain(): Observable<R> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

inline fun <R> Observable<R>.ioToMain(crossinline action: (dis: Disposable) -> Unit): Observable<R> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
            action.invoke(it)
        }
}

inline fun <R> Single<R>.ioToMain(): Single<R> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

inline fun <R> Single<R>.ioToMain(crossinline action: (dis: Disposable) -> Unit): Single<R> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
            action.invoke(it)
        }
}

inline fun Completable.ioToMain(): Completable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

inline fun Completable.ioToMain(crossinline action: (dis: Disposable) -> Unit): Completable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
            action.invoke(it)
        }
}

inline fun <R : Cache> Observable<Response<R>>.convertCache(): Observable<R> {
    return this
        .retry { integer, throwable ->
            (throwable is UnknownHostException || throwable is SocketTimeoutException) && integer <= 2
        }
        .map { t: Response<R> ->
            return@map if (t.isSuccessful) {
                t.body().apply {
                    this?.fromCache = t.headers()?.names()?.contains("Cache-Control") == true
                } ?: throw NullPointerException("null Body")
            } else {
                throw ReadhubException(t.code(), t.message())
            }
        }
}

inline fun <R> Observable<Response<R>>.convert(): Observable<R> {
    return this
        .retry { integer, throwable ->
            (throwable is UnknownHostException || throwable is SocketTimeoutException) && integer <= 2
        }
        .map { t: Response<R> ->
            return@map if (t.isSuccessful) {
                t.body() ?: throw NullPointerException("null Body")
            } else {
                throw ReadhubException(t.code(), t.message())
            }
        }
}

