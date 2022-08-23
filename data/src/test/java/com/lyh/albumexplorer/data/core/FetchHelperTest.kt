package com.lyh.albumexplorer.data.core

import app.cash.turbine.test
import com.lyh.albumexplorer.domain.core.ResultError
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.util.concurrent.TimeoutException

class FetchHelperTest {

    private val list = listOf(1, 2, 3)
    private val listLocal = listOf(1, 2, 3, 4)

    @Test
    fun `WHEN get remote data success THEN return ResultSuccess`() = runTest {
        fetchAndStoreLocally(::getRemoteSuccess, ::sync, ::getLocal, null) {}
            .test {
                val result = awaitItem()
                assertTrue(result is ResultSuccess<*>)
                val resultSuccess = result as ResultSuccess<List<Int>>

                assertEquals(list.size, resultSuccess.data.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get remote data failed THEN return ResultFailed`() = runTest {
        fetchAndStoreLocally(::getRemoteFailed, ::sync, ::getLocal, null) {}

            .test {
                val result = awaitItem()
                assertTrue(result is ResultError<*>)
                val resultError = result as ResultError<List<Int>>

                assertEquals(400, resultError.code)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get remote data throws exception, and local data are empty THEN return ResultException`() =
        runTest {
            fetchAndStoreLocally(::getRemoteException, ::sync, ::getLocalEmpty, null) {}
                .test {
                    val result = awaitItem()
                    assertTrue(result is ResultException<*>)
                    val resultError = result as ResultException<List<Int>>

                    assertEquals(TimeoutException::class.java, resultError.throwable::class.java)

                    awaitComplete()
                }
        }

    @Test
    fun `WHEN get remote data throws exception, and local data are present THEN return local data`() =
        runTest {
            fetchAndStoreLocally(::getRemoteException, ::sync, ::getLocal, null) {}
                .test {
                    val result = awaitItem()
                    assertTrue(result is ResultSuccess<*>)
                    val resultSuccess = result as ResultSuccess<List<Int>>

                    assertEquals(listLocal.size, resultSuccess.data.size)

                    awaitComplete()
                }
        }

    private fun getLocal(): List<Int> = listLocal

    private fun getLocalEmpty(): List<Int> = emptyList()

    private fun getRemoteException(): Response<List<Int>> {
        throw TimeoutException()
    }

    private fun getRemoteFailed() = Response.error<List<Int>>(
        400,
        "Bad request".toResponseBody()
    )

    private fun getRemoteSuccess() = Response.success(list)

    private fun sync(newData: List<Int>): List<Int> = newData
}
