package com.exlskills.android.remote

interface GraphCallback<T> {
    fun onFailure(msg: String, code: Int)
    fun onSuccess(respData: T)
}
