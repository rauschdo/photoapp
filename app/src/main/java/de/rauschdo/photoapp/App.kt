package de.rauschdo.photoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import de.rauschdo.photoapp.utility.ClickableLogDebugTree
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(ClickableLogDebugTree(globalTag = "Dev"))
    }
}
