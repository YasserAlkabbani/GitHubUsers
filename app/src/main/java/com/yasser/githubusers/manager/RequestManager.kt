package com.yasser.githubusers.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class RequestResult<out R>(){
    data class Success<T>(val result:T):RequestResult<T>()
    data class Failure(val throwable: Throwable):RequestResult<Nothing>()
}

suspend fun <T>requestWithResult(request:suspend ()->T):RequestResult<T> = withContext(Dispatchers.IO){
    try { RequestResult.Success(request()) }
    catch (t:Throwable){RequestResult.Failure(t)}
}