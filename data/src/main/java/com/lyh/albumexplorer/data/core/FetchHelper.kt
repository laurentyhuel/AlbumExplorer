package com.lyh.albumexplorer.data.core

import com.lyh.albumexplorer.data.remote.core.ApiError
import com.lyh.albumexplorer.data.remote.core.ApiException
import com.lyh.albumexplorer.data.remote.core.ApiSuccess
import com.lyh.albumexplorer.data.remote.core.callApi
import com.lyh.albumexplorer.domain.core.Result
import com.lyh.albumexplorer.domain.core.ResultError
import com.lyh.albumexplorer.domain.core.ResultException
import com.lyh.albumexplorer.domain.core.ResultSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.util.*

/**
 * Helper method, to fetch data from API and sync with local data,
 * remote fetch is done if last fetch is more than x minutes,
 * if remote fetch returns error then return local data if exists
 * @param getRemote method to fetch remote data
 * @param syncData method to synchronize remote data with local data
 * @param getLocal method to get local data
 * @param lastFetch timestamp of last successful fetch
 * @param fetchRemoteSuccessCallback callback when fetch successful (to store last fetch timestamp in repository)
 * @return return a [Flow] of [Result]
 */
internal fun <DTO, MODEL> fetchAndStoreLocally(
    getRemote: suspend () -> Response<List<DTO>>,
    syncData: suspend (List<DTO>) -> List<MODEL>,
    getLocal: suspend () -> List<MODEL>,
    lastFetch: Date?,
    fetchRemoteSuccessCallback: () -> Unit
): Flow<Result<List<MODEL>>> = flow {

    if (!isNeedingFreshRemoteData(lastFetch)) {
        emit(ResultSuccess(getLocal()))
        return@flow
    }

    when (val apiResult = callApi { getRemote() }) {
        is ApiSuccess -> {
            val models = syncData(apiResult.data)
            fetchRemoteSuccessCallback()
            emit(ResultSuccess(models))
        }
        is ApiError -> emit(ResultError(apiResult.code, apiResult.message))
        is ApiException -> {
            val localData = getLocal()
            if (localData.isNotEmpty()) {
                emit(ResultSuccess(localData))
            } else {
                emit(ResultException(apiResult.throwable))
            }
        }
    }
}

private fun isNeedingFreshRemoteData(lastRemoteSuccess: Date?): Boolean {
    if (lastRemoteSuccess == null) {
        return true
    }
    val nowLessOneHour = Calendar.getInstance().apply {
        // For testing set low value: 10s
        add(Calendar.SECOND, -10)
    }
    return lastRemoteSuccess.before(nowLessOneHour.time)
}
