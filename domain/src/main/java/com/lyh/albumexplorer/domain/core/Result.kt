package com.lyh.albumexplorer.domain.core

sealed interface Result<T>

class ResultSuccess<T>(val data: T) : Result<T>
class ResultError<T>(val code: Int, val message: String) : Result<T>
class ResultException<T>(val throwable: Throwable) : Result<T>