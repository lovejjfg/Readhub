package com.lovejjfg.readhub

import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import java.util.logging.Logger

/**
 * ReadHub
 * Created by Joe at 2017/7/26.
 */
object DataManager {

    private val API_RELEASE = "https://api.readhub.me/"
    private var retrofit: Retrofit? = null
    private val isDebug = true

    fun <T> init(clazz: Class<T>): T {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(API_RELEASE)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!.create(clazz)
    }

    fun init(): ReadHubService {
        return init(ReadHubService::class.java)
    }

    fun <R> convert(request: Observable<Response<R>>): Observable<R> {

        return request
                .subscribeOn(Schedulers.io())//事件产生在子线程
                .retry({ integer, throwable ->
                    (throwable is UnknownHostException || throwable is SocketTimeoutException) && integer <= 2
                })
                .map{t: Response<R> ->
                    t.body()!!
                }
    }

    fun <R> subscribe(request: Observable<Response<R>>,onNext:Consumer<R>,onError:Consumer<Throwable>): Disposable  {
        return convert(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError)




    }




}