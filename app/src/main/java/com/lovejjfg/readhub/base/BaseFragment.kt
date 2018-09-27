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

package com.lovejjfg.readhub.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.lovejjfg.readhub.utils.ErrorUtil
import com.lovejjfg.readhub.utils.http.ToastUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Joe on 2016/10/13.
 * Email lovejjfg@gmail.com
 */

abstract class BaseFragment : Fragment(), IBaseView {
    private var mDisposables: CompositeDisposable = CompositeDisposable()
    var mContext: Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        initFragments(savedInstanceState, this)
        afterCreatedView(savedInstanceState)
    }

    private fun initFragments(savedInstanceState: Bundle?, fragment: BaseFragment) {
        if (savedInstanceState == null) {
            return
        }
        activity?.apply {
            val isSupportHidden = savedInstanceState.getBoolean(BaseFragment.ARG_IS_HIDDEN)
            val ft = this.supportFragmentManager.beginTransaction()
            if (isSupportHidden) {
                ft.hide(fragment)
            } else {
                ft.show(fragment)
            }
            ft.commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(ARG_IS_HIDDEN, isHidden)
        super.onSaveInstanceState(outState)
    }

    override fun showToast(toast: String) {
        ToastUtil.showToast(activity, toast)
    }

    override fun showToast(stringId: Int) {
        ToastUtil.showToast(activity, getString(stringId))
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
        ErrorUtil.handleError(activity, throwable)
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {
    }

    override fun afterCreatedView(savedInstanceState: Bundle?) {
    }

    override fun getMyContext(): Context? {
        return mContext
    }

    companion object {
        const val ARG_IS_HIDDEN = "ARG_IS_HIDDEN"
        const val TAG = "name"
    }
}
