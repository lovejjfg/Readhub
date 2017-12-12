package com.lovejjfg.readhub.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lovejjfg.readhub.utils.ErrorUtil
import com.lovejjfg.readhub.utils.http.ToastUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by joe on 2017/9/28.
 * Email: lovejjfg@gmail.com
 */

abstract class BaseActivity : AppCompatActivity(), IBaseView {
    var mDisposables: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDisposables?.clear()
        mDisposables = CompositeDisposable()

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

    override fun beforeCreate(savedInstanceState: Bundle) {
    }

    override fun afterCreatedView(savedInstanceState: Bundle) {
    }


}
