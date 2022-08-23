package com.lyh.albumexplorer.data.remote.core

sealed interface ApiResult<T>

class ApiSuccess<T>(val data: T) : ApiResult<T>
class ApiError<T>(val code: Int, val message: String) : ApiResult<T>
class ApiException<T>(val throwable: Throwable) : ApiResult<T>