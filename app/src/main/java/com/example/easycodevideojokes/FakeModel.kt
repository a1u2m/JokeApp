package com.example.easycodevideojokes

import java.util.Timer
import java.util.TimerTask

class FakeModel(
    private val manageResources: ManageResources
) : Model<Joke, Error> {

    private val noConnection = Error.NoConnection(manageResources)
    private val serviceUnavailable = Error.ServiceUnavailable(manageResources)
    private var callback: ResultCallback<Joke, Error>? = null
    private var count = 0

    override fun fetch() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (count % 2 == 1) {
                    callback?.provideSuccess(Joke("fake joke $count", "punchline"))
                } else if (count % 3 == 0) {
                    callback?.provideError(noConnection)
                } else {
                    callback?.provideError(serviceUnavailable)
                }
                count++
            }
        }, 2000)
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }


}