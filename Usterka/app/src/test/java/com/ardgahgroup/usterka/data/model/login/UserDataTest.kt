package com.ardgahgroup.usterka.data.model.login

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserDataTest {

    @Test
    fun checkParametr(){
        val userData = UserData(0, "", "", "")
        val result = userData.Id
        assertEquals(result, 0)
    }
}