package com.example.easycodevideojokes

class BaseModel(
    private val jokeService: JokeService,
    private val manageResources: ManageResources
) : Model<Joke, Error> {

    private var callback: ResultCallback<Joke, Error>? = null
    private val noConnection by lazy { Error.NoConnection(manageResources) }
    private val serviceError by lazy { Error.ServiceUnavailable(manageResources) }

    override fun fetch() {
        jokeService.joke(object : ServiceCallback {
            override fun returnSucces(data: JokeCloud) {
                callback?.provideSuccess(data.toJoke())
            }

            override fun returnError(errorType: ErrorType) {
                when (errorType) {
                    ErrorType.NO_CONNECTION -> callback?.provideError(noConnection)
                    ErrorType.OTHER -> callback?.provideError(serviceError)
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