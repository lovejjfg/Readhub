package com.lovejjfg.readhub.utils;

import android.content.Context;
import android.content.Intent;

import com.lovejjfg.readhub.view.WebActivity;

/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

public class JumpUitl {

    public static void jumpWeb(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        context.startActivity(intent);
    }
}
