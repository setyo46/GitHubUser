package com.setyo.githubuser.core.di

import androidx.room.Room
import com.setyo.githubuser.core.BuildConfig
import com.setyo.githubuser.core.data.GithubUserRepository
import com.setyo.githubuser.core.data.source.local.LocalDataSource
import com.setyo.githubuser.core.data.source.local.room.UserRoomDatabase
import com.setyo.githubuser.core.data.source.remote.RemoteDataSource
import com.setyo.githubuser.core.data.source.remote.network.ApiService
import com.setyo.githubuser.core.domain.repository.IGithubUserRepository
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<UserRoomDatabase>().favoriteUserDao() }
    val passphrase: ByteArray = SQLiteDatabase.getBytes("users".toCharArray())
    val factory = SupportFactory(passphrase)
    single {
        Room.databaseBuilder(
            androidContext(),
            UserRoomDatabase ::class.java, "Users.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "token ${BuildConfig.GIHUB_TOKEN}")
                .build()
            chain.proceed(requestHeaders)
        }

        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val hostname = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/jFaeVpA8UQuidlJkkpIdq3MPwD0m8XbuCRbJlezysBE=")
            .add(hostname, "sha256/Wec45nQiFwKvHtuHxSAMGkt19k+uPSw9JlEkxhvYPHk=")
            .add(hostname, "sha256/Jg78dOE+fydIGk19swWwiypUSR6HWZybfnJG/8G7pyM=")
            .add(hostname, "sha256/e0IRz5Tio3GA1Xs4fUVWmH1xHDiH2dMbVtCBSkOIdqM=")
            .build()

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client((get()))
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IGithubUserRepository> {
        GithubUserRepository(
            get(),
            get()
        )
    }
}