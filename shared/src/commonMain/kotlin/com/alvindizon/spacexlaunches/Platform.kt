package com.alvindizon.spacexlaunches

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform