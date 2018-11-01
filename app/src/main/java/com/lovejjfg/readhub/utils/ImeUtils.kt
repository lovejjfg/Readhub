package com.lovejjfg.readhub.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.ResultReceiver
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Utility methods for working with the keyboard
 */
object ImeUtils {

    fun showIme(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // the public methods don't seem to work for me, so… reflection.
        try {
            val showSoftInputUnchecked = InputMethodManager::class.java.getMethod(
                "showSoftInputUnchecked", Int::class.javaPrimitiveType, ResultReceiver::class.java
            )
            showSoftInputUnchecked.isAccessible = true
            showSoftInputUnchecked.invoke(imm, 0, null)
        } catch (e: Exception) {
            // ho hum
        }
    }

    fun hideIme(view: View) {
        try {
            val imm = view.context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun addKeyboardListener(activity: Activity, targetView: View? = null, dy: Int = 0, listener: InputCallback?) {
        try {
            val decorView = activity.window.decorView
            decorView.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                decorView.getWindowVisibleDisplayFrame(rect)
                val rootHeight = decorView.rootView.height
                val mainInvisibleHeight = rootHeight - rect.bottom
                if (mainInvisibleHeight > rootHeight / 4) {
                    val location = IntArray(2)
                    var srollHeight = 0
                    if (targetView != null) {
                        targetView.getLocationInWindow(location)
                        srollHeight = location[1] + targetView.height + dy - rect.bottom
                    }
                    if (srollHeight > 0) {
                        decorView.scrollTo(0, srollHeight)
                    }
                    listener?.invoke(true)
                } else {
                    if (targetView != null) {
                        decorView.scrollTo(0, 0)
                    }
                    listener?.invoke(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeKeyboardListener(activity: Activity) {
        val decorView = activity.window.decorView
        decorView.viewTreeObserver.removeOnGlobalLayoutListener {

        }
    }
}
private typealias InputCallback = ((Boolean) -> Unit)
