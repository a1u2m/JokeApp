package com.example.easycodevideojokes.data

import com.example.easycodevideojokes.presentation.JokeUi
import com.example.easycodevideojokes.presentation.ManageResources

//class FakeRepository(
//    private val manageResources: ManageResources
//) : Repository<JokeUi, Error> {
//
//    private var callback: ResultCallback<JokeUi, Error>? = null
//
//    private val serviceError by lazy {
//        Error.ServiceUnavailable(manageResources)
//    }
//
//    private var count = 0
//
//    override fun fetch() {
//        when (++count % 3) {
//            0 -> callback?.provideSuccess(JokeUi.Base("test text$count", ""))
//            1 -> callback?.provideSuccess(JokeUi.Favorite("favorite $count", ""))
//            2 -> callback?.provideError(serviceError)
//        }
//    }
//
//    override fun clear() {
//        callback = null
//    }
//
//    override fun changeJokeStatus(resultCallback: ResultCallback<JokeUi, Error>) = Unit
//    override fun chooseFavorites(favorites: Boolean) = Unit
//
//    override fun init(resultCallback: ResultCallback<JokeUi, Error>) {
//        callback = resultCallback
//    }
//}