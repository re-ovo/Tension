package me.rerere.tension

import android.app.Application
import me.rerere.tension.di.networkModule
import me.rerere.tension.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TensionApp : Application(){
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@TensionApp)
            androidLogger()
            modules(
                networkModule, viewModelModule
            )
        }
    }
}