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

package com.lovejjfg.readhub.utils.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * Created by Joe on 2015/3/13.
 * Email lovejjfg@gmail.com
 */
public class NetWorkUtils {
    @Nullable
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static boolean isNetWorkAvailable() {
        ConnectivityManager cm = null;
        if (mContext != null) {
            cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (cm != null) {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = null;
        if (mContext != null) {
            mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo mNetworkInfo = null;
        if (mConnectivityManager != null) {
            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        }
        return mNetworkInfo != null && mNetworkInfo.isAvailable();
    }


}
