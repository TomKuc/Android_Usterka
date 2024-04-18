package com.ardgahgroup.usterka.data.model.api

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlaceModelTest {

    @Test
    fun checkParametr(){
        val placeModel = PlaceModel()
        val result = placeModel.iD_parent
        assertEquals(result, 0)
    }
}