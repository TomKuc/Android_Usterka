package com.ardgahgroup.usterka.data.retrofit

import android.util.Log
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.api.LoginModel
import com.ardgahgroup.usterka.ui.activities.login.LoginResult
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitClientTest {

    @Test
    fun getInstanceLogin() {
        RetrofitClient.getInstanceLogin()
    }

    @Test
    fun getInstance() {
        val token: String = ""
        RetrofitClient.getInstance(token)
    }

}