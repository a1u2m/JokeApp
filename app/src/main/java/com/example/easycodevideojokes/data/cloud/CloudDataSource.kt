package com.example.easycodevideojokes.data.cloud

import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.cache.DataSource
import com.example.easycodevideojokes.data.cache.JokeCallback
import com.example.easycodevideojokes.presentation.ManageResources
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource : DataSource {

    class Base(
        private val jokeService: JokeService,
        private val manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceError by lazy { Error.ServiceUnavailable(manageResources) }

        override fun fetch(jokeCallback: JokeCallback) {
            jokeService.joke().enqueue(object : Callback<JokeCloud> {
                override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null)
                            jokeCallback.provideError(serviceError)
                        else
                            jokeCallback.provideJoke(body)
                    } else
                        jokeCallback.provideError(serviceError)
                }

                override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                    jokeCallback.provideError(
                        if (t is UnknownHostException || t is ConnectException)
                            noConnection
                        else
                            serviceError
                    )
                }
            })
        }
    }
}