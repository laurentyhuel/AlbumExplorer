package com.lyh.albumexplorer.data.remote.core

import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.util.concurrent.TimeoutException

class ApiHelperTest {

    @Test
    fun `WHEN api is successful THEN return ApiSuccess`() = runTest {

        val result = callApi { Response.success(99) }
        assertTrue(result is ApiSuccess)
        val apiSuccess = result as ApiSuccess
        assertEquals(99, apiSuccess.data)
    }

    @Test
    fun `WHEN api failed THEN return ApiError`() = runTest {
        val result: ApiResult<Any> = callApi { Response.error(400, "Bad request".toResponseBody()) }

        assertTrue(result is ApiError)
        result as ApiError
        assertEquals(400, result.code)
    }

    @Test
    fun `WHEN api throws exception THEN return ApiException`() = runTest {
        val result: ApiResult<Any> = callApi { throw TimeoutException() }

        assertTrue(result is ApiException)
        val resultException = result as ApiException
        assertEquals(TimeoutException::class.java, resultException.throwable::class.java)
    }
}
