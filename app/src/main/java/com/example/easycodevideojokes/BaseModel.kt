package com.example.easycodevideojokes

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

class BaseModel(
    private val jokeService: JokeService,
    private val manageResources: ManageResources
) : Model<Joke, Error> {

    private var callback: ResultCallback<Joke, Error>? = null
    private val noConnection by lazy { Error.NoConnection(manageResources) }
    private val serviceError by lazy { Error.ServiceUnavailable(manageResources) }

    override fun fetch() {
        jokeService.joke().enqueue(object : Callback<JokeCloud> {
            override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null)
                        callback?.provideError(serviceError)
                    else
                        callback?.provideSuccess(body.toJoke())
                } else {
                    callback?.provideError(serviceError)
                }
            }

            override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                if (t is UnknownHostException || t is ConnectException) {
                    callback?.provideError(noConnection)
                } else {
                    callback?.provideError(serviceError)
                }
            }
        })
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }
}