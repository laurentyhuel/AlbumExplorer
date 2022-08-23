package com.lyh.albumexplorer.data.core

import kotlinx.coroutines.CoroutineDispatcher

class AppDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
)