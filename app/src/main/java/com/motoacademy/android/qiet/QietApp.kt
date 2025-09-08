package com.motoacademy.android.qiet

import android.app.Application
import com.motoacademy.android.qiet.di.ApplicationComponent
import com.motoacademy.android.qiet.di.DaggerApplicationComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QietApp : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}
