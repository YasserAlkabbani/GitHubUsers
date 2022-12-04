package com.yasser.githubusers

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class GitHubUsersApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

}