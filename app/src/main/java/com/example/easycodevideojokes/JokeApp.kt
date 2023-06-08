package com.example.easycodevideojokes

import android.app.Application
import com.example.easycodevideojokes.data.BaseRepository
import com.example.easycodevideojokes.data.FakeRepository
import com.example.easycodevideojokes.data.cache.CacheDataSource
import com.example.easycodevideojokes.data.cache.ProvideRealm
import com.example.easycodevideojokes.data.cloud.CloudDataSource
import com.example.easycodevideojokes.data.cloud.JokeService
import com.example.easycodevideojokes.presentation.MainViewModel
import com.example.easycodevideojokes.presentation.ManageResources
import io.realm.Realm
import leakcanary.LeakCanary
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApp : Application() {

    lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        ManageResources.Base(this)
        val retrofit =
            Retrofit.Builder().baseUrl("https://official-joke-api.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val manageResources = ManageResources.Base(this)
        viewModel = MainViewModel(
            BaseRepository(
                CloudDataSource.Base(
                    retrofit.create(JokeService::class.java),
                    ManageResources.Base(this)
                ),
                CacheDataSource.Base(object : ProvideRealm {
                    override fun provideRealm(): Realm = Realm.getDefaultInstance()
                }, manageResources)
            )
//            BaseModel(
//                retrofit.create(JokeService::class.java),
//                ManageResources.Base(this)
//            )
        )
    }
}