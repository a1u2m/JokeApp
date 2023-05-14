package com.example.easycodevideojokes.data

import androidx.annotation.StringRes
import com.example.easycodevideojokes.presentation.ManageResources
import com.example.easycodevideojokes.R

interface Error {

    fun message(): String

    abstract class Abstract(
        private val manageResources: ManageResources,
        @StringRes private val messageId: Int
    ) : Error {
        override fun message() = manageResources.string(messageId)
    }

    class NoConnection(manageResources: ManageResources) :
        Abstract(manageResources, R.string.no_internet_message)

    class ServiceUnavailable(private val manageResources: ManageResources) :
        Abstract(manageResources, R.string.service_unavailable_message)

    class NoFavoriteJoke(manageResources: ManageResources): Abstract(manageResources, R.string.no_faviorite_joke)
}