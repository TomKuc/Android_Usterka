package com.ardgahgroup.usterka.data.model.api.healthCheck

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HealthCheckResponseTest {

    @Test
    fun checkParametr(){
        val healthCheckResponse = HealthCheckResponse()
        val result = healthCheckResponse.checks
        assertEquals(result, null)
    }
}