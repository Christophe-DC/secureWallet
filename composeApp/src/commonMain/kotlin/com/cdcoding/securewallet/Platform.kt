package com.cdcoding.securewallet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform