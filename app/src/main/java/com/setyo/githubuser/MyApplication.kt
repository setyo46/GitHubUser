package com.setyo.githubuser

import android.app.Application
import com.setyo.githubuser.core.di.databaseModule
import com.setyo.githubuser.core.di.networkModule
import com.setyo.githubuser.core.di.repositoryModule
import com.setyo.githubuser.di.useCaseModule
import com.setyo.githubuser.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}