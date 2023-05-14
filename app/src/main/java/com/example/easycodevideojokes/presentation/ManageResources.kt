package com.example.easycodevideojokes.presentation

import android.content.Context
import androidx.annotation.StringRes

interface ManageResources {

    fun string(@StringRes resourceId: Int) : String

    class Base(private val context: Context) : ManageResources {
        override fun string(@StringRes resourceId: Int) = context.getString(resourceId)
    }

}