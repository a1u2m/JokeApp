package com.example.easycodevideojokes.data.cloud

import com.example.easycodevideojokes.data.cache.CacheDataSource
import com.example.easycodevideojokes.presentation.JokeUi
import com.google.gson.annotations.SerializedName

data class JokeCloud(
    @SerializedName("type")
    val type: String,
    @SerializedName("setup")
    val mainText: String,
    @SerializedName("punchline")
    val punchline: String,
    @SerializedName("id")
    val id: Int
) {

    fun toUi(): JokeUi =
        JokeUi.Base(mainText, punchline)
    fun toFavoriteUi() : JokeUi = JokeUi.Favorite(mainText, punchline)

    fun change(cacheDataSource: CacheDataSource) =
        cacheDataSource.addOrRemove(id, this)


}