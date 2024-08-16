package com.cdcoding.common.utils

import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSSearchPathDirectory
import platform.Foundation.NSSearchPathDomainMask
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSSearchPathForDirectoriesInDomains

class IOSDataDir(): DataDir {
    override val path: String
        get() {
            val paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, true)
            return paths.first() as String
        }

}