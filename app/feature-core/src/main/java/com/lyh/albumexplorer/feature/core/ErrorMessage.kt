package com.lyh.albumexplorer.feature.core

import android.content.Context

sealed class ErrorMessage {
    data class ErrorMessageString(val message: String) : ErrorMessage()
    data class ErrorMessageResource(val messageResource: Int) : ErrorMessage()

    fun getMessage(context: Context): String = when (this) {
        is ErrorMessageString -> message
        is ErrorMessageResource -> context.getString(messageResource)
    }
}