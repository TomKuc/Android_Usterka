package com.ardgahgroup.usterka.data

import com.ardgahgroup.usterka.data.model.login.UserData
import org.junit.Assert.*
import org.junit.Test

class LoginRepositoryTest {

    @Test
    fun logout() {
        val userData = UserData(1, "Department", "Role", "Token")
        LoginRepository.userData = userData
        LoginRepository.logout()
        val result = userData.Id
        assertEquals(result, 0)
    }
}