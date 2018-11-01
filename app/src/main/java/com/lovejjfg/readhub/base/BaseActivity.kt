package com.lovejjfg.readhub.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.utils.ErrorUtil
import com.lovejjfg.readhub.utils.RxBus
import com.lovejjfg.readhub.utils.getStatusBarHeight
import com.lovejjfg.readhub.utils.http.ToastUtil
import com.lovejjfg.readhub.view.widget.SwipeCoordinatorLayout
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_statusbar.statusBarProxy

/**
 * Created by joe on 2017/9/28.
 * Email: lovejjfg@gmail.com
 */

abstract class BaseActivity : AppCompatActivity(), IBaseView {
    protected val mDisposables: CompositeDisposable = CompositeDisposable()
    final override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        afterCreatedView(savedInstanceState)
        initStatusBar()
        initToolBar()
    }

    private fun initToolBar() {
        (findViewById<View>(R.id.toolbar) as? Toolbar)?.setNavigationOnClickListener { onBackPressed() }
        (findViewById<View>(R.id.parentContainer) as? SwipeCoordinatorLayout)?.setOnSwipeBackListener {
            onBackPressed()
        }
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
        mDisposables.add(subscriber)
    }

    override fun unSubscribe() {
        mDisposables.clear()
    }

    override fun handleError(throwable: Throwable) {
        ErrorUtil.handleError(this, throwable)
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {
        mDisposables.clear()
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
    }

    override fun onDestroy() {
        RxBus.instance.unSubscribe(this)
        super.onDestroy()
    }

    override fun getMyContext(): Context? {
        return this
    }

    private fun initStatusBar() {
        statusBarProxy?.let {
            val statusBarHeight = getStatusBarHeight()
            val params = statusBarProxy.layoutParams
            params.height = statusBarHeight
            statusBarProxy.layoutParams = params
            val layoutParams = (findViewById<View>(R.id.toolbar) as? Toolbar)?.layoutParams as? MarginLayoutParams
            layoutParams?.topMargin = statusBarHeight

        }
    }
}
