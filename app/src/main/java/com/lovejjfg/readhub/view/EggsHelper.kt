/*
 * Copyright (c) 2019.  Joe
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

package com.lovejjfg.readhub.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.graphics.Color
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import com.lovejjfg.readhub.utils.FirebaseUtils
import com.lovejjfg.readhub.utils.dip2px
import com.lovejjfg.readhub.utils.getScreenHeight
import com.lovejjfg.readhub.utils.getScreenWidth
import com.tencent.bugly.crashreport.CrashReport

/**
 * Created by joe on 2019/2/18.
 * Email: lovejjfg@gmail.com
 */
object EggsHelper {
    private val emojis = arrayListOf(
        "\uD83E\uDD13", "\uD83D\uDE0F", "\uD83D\uDE09",
        "\uD83D\uDE0E", "\uD83D\uDE43", "\uD83E\uDD2A"
    )

    fun showRandomEgg(activity: Activity?) {
        try {
            if (activity == null) {
                return
            }
            val count = (Math.random() * 8).toInt() + 5
            for (i in 0..count) {
                addRandomFloatView(activity)
            }
            FirebaseUtils.logEggRandom(activity)
        } catch (e: Exception) {
            CrashReport.postCatchedException(e)
        }
    }

    var isRunning: Boolean = false
    fun showCenterScaleView(activity: Activity?): Boolean {
        try {
            if (activity == null) {
                return false
            }
            if (isRunning) {
                return false
            }
            isRunning = true
            val layoutParams = createLayoutParams()
            layoutParams.gravity = Gravity.CENTER
            val emoji = generateEmoji(activity, 30f, layoutParams)
            addView(activity, emoji)
            emoji.animate()
                .scaleX(2f)
                .scaleY(2f)
                .setInterpolator(BounceInterpolator())
                .setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        hideFloatView(activity, emoji)
                        isRunning = false
                    }
                })
                .start()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun addRandomFloatView(activity: Activity) {
        val randomTransitionX = (Math.random() * (activity.getScreenWidth() - activity.dip2px(20f))).toFloat()
        val textView = generateEmoji(activity)
        addView(activity, textView)
        textView.translationX = randomTransitionX
        textView.animate()
            .translationY(activity.getScreenHeight().toFloat() + 300f)
            .rotation(360f)
            .setDuration(2000)
            .setInterpolator(FastOutSlowInInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    hideFloatView(activity, textView)
                }
            })
            .start()
    }

    private fun generateEmoji(
        activity: Activity,
        textsize: Float = 22f,
        layoutParams: LayoutParams = createLayoutParams(),
        emoji: String = emojis[(Math.random() * emojis.size).toInt()]
    ): TextView {
        val textView = TextView(activity).apply {
            textSize = textsize
            setLayoutParams(layoutParams)
        }
        textView.text = emoji
        textView.setTextColor(Color.WHITE)

        return textView
    }

    private fun addView(
        activity: Activity,
        textView: TextView
    ) {
        (activity.window.decorView as? ViewGroup)?.addView(textView)
    }

    private fun hideFloatView(activity: Activity, textView: TextView) {
        (activity.window.decorView as? ViewGroup)?.removeView(textView)
    }

    private fun createLayoutParams(
        width: Int = LayoutParams.WRAP_CONTENT,
        height: Int = LayoutParams.WRAP_CONTENT
    ): FrameLayout
    .LayoutParams {
        return FrameLayout.LayoutParams(
            width,
            height
        )
    }
}
