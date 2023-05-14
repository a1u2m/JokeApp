package com.example.easycodevideojokes

import com.example.easycodevideojokes.data.Error
import com.example.easycodevideojokes.data.Repository
import com.example.easycodevideojokes.data.ResultCallback
import com.example.easycodevideojokes.presentation.JokeUi
import com.example.easycodevideojokes.presentation.MainViewModel
import com.example.easycodevideojokes.presentation.JokeUiCallback
import org.junit.Assert.*
import org.junit.Test

class MainViewRepositoryTest {

    @Test
    fun test_success() {
        val model = com.example.easycodevideojokes.data.FakeRepository()
        model.returnSucces = true
        val viewModel = MainViewModel(model)
        viewModel.init(object : JokeUiCallback {
            override fun provideText(text: String) {
                assertEquals("fake joke 1" + "\n" + "punchline", text)
            }
        })
        viewModel.getJoke()
    }

    @Test
    fun test_error() {
        val model = com.example.easycodevideojokes.data.FakeRepository()
        model.returnSucces = false
        val viewModel = MainViewModel(model)
        viewModel.init(object : JokeUiCallback {
            override fun provideText(text: String) {
                assertEquals("fake error message", text)
            }
        })
        viewModel.getJoke()
    }
}

private class FakeRepository : Repository<JokeUi, Error> {

    var returnSucces = true
    private var callback: ResultCallback<JokeUi, Error>? = null

    override fun fetch() {
        if (returnSucces)
            callback?.provideSuccess(JokeUi("fake joke 1", "punchline"))
        else
            callback?.provideError(FakeError())
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<JokeUi, Error>) {
        callback = resultCallback
    }
}

private class FakeError : Error {
    override fun message(): String {
        return "fake error message"
    }
}