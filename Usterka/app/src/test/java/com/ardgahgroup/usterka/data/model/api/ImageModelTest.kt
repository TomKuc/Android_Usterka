package com.ardgahgroup.usterka.data.model.api

import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImageModelTest {

    @Test
    fun checkParametr(){
        val imageModel = ImageModel()
        val result = imageModel.iD_miejsca
        assertEquals(result, null)
    }

}