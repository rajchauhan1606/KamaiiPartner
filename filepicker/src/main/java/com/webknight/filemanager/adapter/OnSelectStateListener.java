/*
 * *
 *  * Created by WebKnight Infosystem on 14/5/20 10:52 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 14/5/20 10:37 AM
 *
 */

package com.webknight.filemanager.adapter;

public interface OnSelectStateListener<T> {
    void OnSelectStateChanged(boolean state, T file);
}
