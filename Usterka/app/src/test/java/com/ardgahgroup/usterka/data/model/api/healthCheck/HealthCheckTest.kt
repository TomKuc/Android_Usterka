package com.ardgahgroup.usterka.data.model.api.healthCheck

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HealthCheckTest {

    /*
    @Test
    fun checkParametr(){
        val healthCheck = HealthCheck("status", "compotent", "description", "exception", null)
        val result = healthCheck.compotent
        assertEquals(result, "compotent")
    }*/

    @Test
    fun checkParametr(){
        val healthCheck = HealthCheck()
        val result = healthCheck.compotent
        assertEquals(result, "")
    }
}