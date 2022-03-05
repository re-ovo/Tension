package me.rerere.tension

import android.app.Application
import android.content.Context
import me.rerere.tension.di.networkModule
import me.rerere.tension.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TensionApp : Application(){
    override fun onCreate() {
        super.onCreate()

        instance = this

        // Start Koin
        startKoin {
            androidContext(this@TensionApp)
            androidLogger()
            modules(
                networkModule, viewModelModule
            )
        }
    }

    companion object {
        lateinit var instance: TensionApp
    }
}

/**
 * Get the sharedPreferences of the application context
 */
fun sharedPreferences(name: String) = TensionApp.instance.getSharedPreferences(name, Context.MODE_PRIVATE)