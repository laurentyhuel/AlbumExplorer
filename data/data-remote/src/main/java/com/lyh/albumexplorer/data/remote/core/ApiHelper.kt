package com.lyh.albumexplorer.data.remote.core

import retrofit2.Response

/**
 * Helper method to invoke Retrofit call and map result to [ApiResult]
 * @param api retrofit call
 * @return api result
 */
suspend fun <T> callApi(
    api: suspend () -> Response<T>
): ApiResult<T> = try {
    val response = api.invoke()
    val body: T? = response.body()
    if (response.isSuccessful && body != null) {
        ApiSuccess(body)
    } else {
        ApiError(response.code(), response.message())
    }
} catch (exception: Exception) {
    ApiException(exception)
}

