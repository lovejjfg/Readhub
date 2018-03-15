package com.lovejjfg.readhub.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.view.Gravity
import android.view.Window
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.ErrorUtil
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.event.NoNetEvent
import com.lovejjfg.readhub.utils.http.ToastUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by joe on 2017/9/28.
 * Email: lovejjfg@gmail.com
 */

abstract class BaseActivity : AppCompatActivity(), IBaseView {
    private var mDisposables: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            window.enterTransition = Slide(Gravity.RIGHT)
            window.exitTransition = Slide(Gravity.LEFT)
        }
        beforeCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        afterCreatedView(savedInstanceState)

    }

    override fun showToast(toast: String) {
        ToastUtil.showToast(this, toast)
    }

    override fun showToast(stringId: Int) {
        ToastUtil.showToast(this, getString(stringId))
    }

    override fun showLoadingDialog(msg: String) {
    }

    override fun showLoadingDialog(cancelable: Boolean) {
    }


    override fun closeLoadingDialog() {
    }


    override fun subscribe(subscriber: Disposable) {
        mDisposables?.add(subscriber)
    }

    override fun unSubscribe() {
        mDisposables?.clear()
    }

    override fun handleError(throwable: Throwable) {
        ErrorUtil.handleError(this, throwable)
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {
        mDisposables?.clear()
        mDisposables = CompositeDisposable()
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
        RxBus.instance.addSubscription(this, NoNetEvent::class.java, Consumer {
            showToast(R.string.net_unavailable)
        }, Consumer {
            it.printStackTrace()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.instance.unSubscribe(this)
    }

    override fun getMyContext(): Context? {
        return this
    }


}
