package com.exlskills.android.com.exlskills.android.remote

interface AuthCallback {
    fun onFailure(msg: String, code: Int)
    fun onSuccess(jwt: String, userData: UserData)
}
