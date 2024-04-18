package com.ardgahgroup.usterka.data.model.api

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ApiUserDataTest {
    @Test
    fun checkParametr(){
        val apiUserData = ApiUserData()
        val result = apiUserData.dział
        assertEquals(result, "")
    }
}