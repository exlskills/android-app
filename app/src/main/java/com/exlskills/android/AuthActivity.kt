package com.exlskills.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.exlskills.android.remote.Graph

class AuthActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    // TODO check if user logged in:
    //      Logged in: send to MainActivity
    //      Not:       send to the LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

}
