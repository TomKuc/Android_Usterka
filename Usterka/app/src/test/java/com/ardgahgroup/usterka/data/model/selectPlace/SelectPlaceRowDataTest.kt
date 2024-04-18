package com.ardgahgroup.usterka.data.model.selectPlace

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SelectPlaceRowDataTest {

    @Test
    fun checkParametr(){
        val selectPlaceRowData = SelectPlaceRowData("", 0, null)
        val result = selectPlaceRowData.idParent
        assertEquals(result, null)
    }

}