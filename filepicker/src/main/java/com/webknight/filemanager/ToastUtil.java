/*
 * *
 *  * Created by WebKnight Infosystem on 14/5/20 10:52 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 14/5/20 10:37 AM
 *
 */

package com.webknight.filemanager;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastUtil {
    private static WeakReference<Context> mContext;
    private static ToastUtil mInstance;
    private Toast mToast;

    public static ToastUtil getInstance(Context ctx) {
        if (mInstance == null || mContext.get() == null) {
            mInstance = new ToastUtil(ctx);
        }

        return mInstance;
    }

    private ToastUtil(Context ctx) {
        mContext = new WeakReference<>(ctx);
    }

    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext.get(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void showToast(int resID) {
        showToast(mContext.get().getResources().getString(resID));
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
