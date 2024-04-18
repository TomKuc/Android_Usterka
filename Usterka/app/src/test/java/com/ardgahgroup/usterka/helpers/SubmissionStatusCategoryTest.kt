package com.ardgahgroup.usterka.helpers

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SubmissionStatusCategoryTest{

    @Test
    fun constructor(){
        val category = SubmissionStatusCategory("Zgłoszone")
        val result = category.categoryId
        assertEquals(result, 2)
    }
}