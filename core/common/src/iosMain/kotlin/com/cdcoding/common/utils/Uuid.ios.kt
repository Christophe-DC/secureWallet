package com.cdcoding.common.utils

import platform.Foundation.NSUUID

actual fun uuid4(): String {
  return NSUUID().UUIDString
}