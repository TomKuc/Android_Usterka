package com.ardgahgroup.usterka.data.model.api

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SubmissionsWithStatusModelTest {

    @Test
    fun checkParametr(){
        val submissionsWithStatusModel = SubmissionsWithStatusModel()
        val result = submissionsWithStatusModel.dzial
        assertEquals(result, null)
    }
}