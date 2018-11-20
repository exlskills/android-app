package com.exlskills.android.com.exlskills.android.remote

import org.json.JSONObject

interface GraphCallback<T> {
    fun onFailure(msg: String, code: Int)
    fun onSuccess(respData: T)
}
