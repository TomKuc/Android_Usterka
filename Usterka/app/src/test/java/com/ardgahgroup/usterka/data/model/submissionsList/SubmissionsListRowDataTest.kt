package com.ardgahgroup.usterka.data.model.submissionsList

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SubmissionsListRowDataTest {

    @Test
    fun checkParametr(){
        val submissionsListRowData = SubmissionsListRowData("", 0)
        val result = submissionsListRowData.id
        assertEquals(result, 0)
    }
}