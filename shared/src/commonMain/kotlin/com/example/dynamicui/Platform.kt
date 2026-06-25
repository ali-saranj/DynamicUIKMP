package com.example.dynamicui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform