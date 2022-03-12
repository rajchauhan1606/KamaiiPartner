/*
 * *
 *  * Created by WebKnight Infosystem on 14/5/20 10:52 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 14/5/20 10:37 AM
 *
 */

package com.webknight.filemanager.filter.callback;
import com.webknight.filemanager.filter.entity.BaseFile;
import com.webknight.filemanager.filter.entity.Directory;

import java.util.List;

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}
