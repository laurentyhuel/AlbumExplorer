package com.lyh.albumexplorer.feature.core

sealed interface Resource<T>

class ResourceSuccess<T>(val data: T) : Resource<T>
class ResourceLoading<T> : Resource<T>
class ResourceError<T>(val errorMessage: ErrorMessage) : Resource<T>





