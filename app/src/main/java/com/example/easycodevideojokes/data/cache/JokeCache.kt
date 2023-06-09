package com.example.easycodevideojokes.data.cache

import com.example.easycodevideojokes.data.Joke
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class JokeCache : RealmObject(), Joke {
    @PrimaryKey
    var id: Int = -1
    var text: String = ""
    var punchline: String = ""
    var type: String = ""

    override suspend fun <T> map(mapper: Joke.Mapper<T>): T = mapper.map(type, text, punchline, id)
}