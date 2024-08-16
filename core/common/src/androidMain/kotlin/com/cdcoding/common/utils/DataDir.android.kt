package com.cdcoding.common.utils

import android.content.Context

class AndroidDataDir(val context: Context): DataDir {
    override val path: String
        get() = context.dataDir.toString()

}