/*
 * *
 *  * Created by WebKnight Infosystem on 14/5/20 10:52 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 14/5/20 10:37 AM
 *
 */

package com.webknight.filemanager.filter;

import androidx.fragment.app.FragmentActivity;

import com.webknight.filemanager.filter.callback.FileLoaderCallbacks;
import com.webknight.filemanager.filter.callback.FilterResultCallback;
import com.webknight.filemanager.filter.entity.AudioFile;
import com.webknight.filemanager.filter.entity.ImageFile;
import com.webknight.filemanager.filter.entity.NormalFile;
import com.webknight.filemanager.filter.entity.VideoFile;

import static com.webknight.filemanager.filter.callback.FileLoaderCallbacks.TYPE_AUDIO;
import static com.webknight.filemanager.filter.callback.FileLoaderCallbacks.TYPE_FILE;
import static com.webknight.filemanager.filter.callback.FileLoaderCallbacks.TYPE_IMAGE;
import static com.webknight.filemanager.filter.callback.FileLoaderCallbacks.TYPE_VIDEO;

public class FileFilter {
    public static void getImages(FragmentActivity activity, FilterResultCallback<ImageFile> callback){
        activity.getSupportLoaderManager().initLoader(0, null,
                new FileLoaderCallbacks(activity, callback, TYPE_IMAGE));
    }

    public static void getVideos(FragmentActivity activity, FilterResultCallback<VideoFile> callback){
        activity.getSupportLoaderManager().initLoader(1, null,
                new FileLoaderCallbacks(activity, callback, TYPE_VIDEO));
    }

    public static void getAudios(FragmentActivity activity, FilterResultCallback<AudioFile> callback){
        activity.getSupportLoaderManager().initLoader(2, null,
                new FileLoaderCallbacks(activity, callback, TYPE_AUDIO));
    }

    public static void getFiles(FragmentActivity activity,
                                FilterResultCallback<NormalFile> callback, String[] suffix){
        activity.getSupportLoaderManager().initLoader(3, null,
                new FileLoaderCallbacks(activity, callback, TYPE_FILE, suffix));
    }
}
