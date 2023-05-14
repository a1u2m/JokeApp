package com.example.easycodevideojokes.data.cloud

import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.cache.ProvideError
import com.example.easycodevideojokes.presentation.ManageResources
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource {

    fun fetch(cloudCallback: JokeCloudCallback)

    class Base(
        private val jokeService: JokeService,
        private val manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceError by lazy { Error.ServiceUnavailable(manageResources) }

        override fun fetch(cloudCallback: JokeCloudCallback) {
            jokeService.joke().enqueue(object : Callback<JokeCloud> {
                override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null)
                            cloudCallback.provideError(serviceError)
                        else
                            cloudCallback.provideJokeCloud(body)
                    } else
                        cloudCallback.provideError(serviceError)
                }

                override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                    cloudCallback.provideError(
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

interface JokeCloudCallback: ProvideError {
    fun provideJokeCloud(jokeCloud: JokeCloud)
}