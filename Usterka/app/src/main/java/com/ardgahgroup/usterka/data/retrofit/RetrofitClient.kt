package com.ardgahgroup.usterka.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class OAuthInterceptor(private val tokenType: String, private val accessToken: String) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $accessToken").build()

        return chain.proceed(request)
    }
}

object RetrofitClient {
    private var instance: CoreApi? = null
    private lateinit var client: OkHttpClient

    private var login: Boolean = false

    fun getInstance(token: String): CoreApi {
        if (instance == null || login) {
            client = OkHttpClient.Builder()
                .addInterceptor(OAuthInterceptor("Bearer", token))
                .build()

            instance = Retrofit.Builder()
                .baseUrl("https://webapiforglass.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(CoreApi::class.java)
        }
        login = false
        return instance!!
    }

    fun getInstanceLogin(): CoreApi {
        client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        instance = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://webapiforglass.azurewebsites.net")
            .client(client)
            .build()
            .create(CoreApi::class.java)

        login = true
        return instance!!
    }
}