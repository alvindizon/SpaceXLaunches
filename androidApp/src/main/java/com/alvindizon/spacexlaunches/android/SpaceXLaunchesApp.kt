package com.alvindizon.spacexlaunches.android

import android.app.Application
import com.alvindizon.spacexlaunches.shared.cache.DatabaseDriverFactory

class SpaceXLaunchesApp: Application() {

    val databaseDriverFactory: DatabaseDriverFactory by lazy {
        DatabaseDriverFactory(this)
    }
}