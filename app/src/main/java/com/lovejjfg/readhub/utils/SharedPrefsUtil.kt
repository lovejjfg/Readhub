/*
 * Copyright (c) 2017.  Joe
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

import android.content.Context
import android.content.SharedPreferences
import androidx.content.edit


internal object SharedPrefsUtil {
    private val SHARE_PRESNAME = "SharedPreferences"

    /**
     * 向SharedPreferences中写入int类型数据
     *
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */
    fun putValue(context: Context, key: String,
                 value: Int) {
        getSharedPreferences(context).edit {
            putInt(key, value)
        }
    }

    /**
     * 向SharedPreferences中写入boolean类型的数据
     *
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */
    fun putValue(context: Context, key: String,
                 value: Boolean) {
        getSharedPreferences(context).edit {
            putBoolean(key, value)
        }
    }

    /**
     * 向SharedPreferences中写入String类型的数据
     *
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */
    fun putValue(context: Context, key: String,
                 value: String) {
        getSharedPreferences(context).edit {
            putString(key, value)
        }
    }

    /**
     * 向SharedPreferences中写入float类型的数据
     *
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */
    fun putValue(context: Context, key: String,
                 value: Float) {
        getSharedPreferences(context).edit {
            putFloat(key, value)
        }
    }

    /**
     * 向SharedPreferences中写入long类型的数据
     *
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */
    fun putValue(context: Context, key: String,
                 value: Long) {
        getSharedPreferences(context).edit {
            putLong(key, value)
        }
    }

    /**
     * 从SharedPreferences中读取int类型的数据
     *
     * @param context  上下文环境
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    fun getValue(context: Context, key: String,
                 defValue: Int): Int {
        val sp = getSharedPreferences(context)
        return sp.getInt(key, defValue)
    }

    /**
     * 从SharedPreferences中读取boolean类型的数据
     *
     * @param context  上下文环境
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    fun getValue(context: Context, key: String,
                 defValue: Boolean): Boolean {
        val sp = getSharedPreferences(context)
        return sp.getBoolean(key, defValue)
    }

    /**
     * 从SharedPreferences中读取String类型的数据
     *
     * @param context  上下文环境
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    fun getValue(context: Context, key: String,
                 defValue: String): String? {
        val sp = getSharedPreferences(context)
        return sp.getString(key, defValue)
    }

    /**
     * 从SharedPreferences中读取float类型的数据
     *
     * @param context  上下文环境
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    fun getValue(context: Context, key: String,
                 defValue: Float): Float {
        val sp = getSharedPreferences(context)
        return sp.getFloat(key, defValue)
    }

    /**
     * 从SharedPreferences中读取long类型的数据
     *
     * @param context  上下文环境
     * @param key      键
     * @param defValue 如果读取不成功则使用默认值
     * @return 返回读取的值
     */
    fun getValue(context: Context, key: String,
                 defValue: Long): Long {
        val sp = getSharedPreferences(context)
        return sp.getLong(key, defValue)
    }

//    //获取Editor实例
//    private fun getEditor(context: Context): Editor {
//        return getSharedPreferences(context).edit()
//    }

    //获取SharedPreferences实例
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARE_PRESNAME, Context.MODE_PRIVATE)
    }

    /**
     * 删除SharedPref文件
     *
     * @param context
     */
    fun deletePref(context: Context) {
        getSharedPreferences(context).edit {
            clear()
        }
    }

    /**
     * 由key删除value
     *
     * @param context
     * @param key
     */
    fun removeKey(context: Context, key: String) {
        getSharedPreferences(context).edit {
            remove(key)
        }
    }
}
