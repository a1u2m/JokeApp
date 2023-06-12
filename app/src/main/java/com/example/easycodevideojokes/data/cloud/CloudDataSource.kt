package com.example.easycodevideojokes.data.cloud

import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.cache.DataSource
import com.example.easycodevideojokes.data.cache.JokeResult
import com.example.easycodevideojokes.presentation.ManageResources
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException

interface CloudDataSource : DataSource {

    class Base(
        private val jokeService: JokeService,
        private val manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceError by lazy { Error.ServiceUnavailable(manageResources) }

        override suspend fun fetch(): JokeResult =
             try {
                val response = jokeService.joke().execute()
                JokeResult.Success(response.body()!!, false)
            } catch (e: Exception) {
                 JokeResult.Failure(
                    if (e is UnknownHostException || e is ConnectException)
                        noConnection
                    else
                        serviceError
                )
            }
    }
}