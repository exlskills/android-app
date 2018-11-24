package com.exlskills.android.com.exlskills.android.remote

import okhttp3.*
import java.io.IOException

class Auth {
    // TODO implement auth
    private val authBaseUrl = "https://auth-api.exlskills.com"
    private val client = OkHttpClient()

    fun authenticate() {
        run(authBaseUrl)
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Encountered failure")
            }
            override fun onResponse(call: Call, response: Response) {
                println("Woah great success!")
                println(response.body()?.string())
            }
        })
    }
}