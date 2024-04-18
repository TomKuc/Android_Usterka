package com.ardgahgroup.usterka.data

import com.ardgahgroup.usterka.data.model.login.UserData

/**
 * Class that maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    companion object {

        lateinit var userData: UserData

        fun logout() {
            userData.Id = 0
            userData.Department = ""
            userData.Role = ""
            userData.Token = ""
        }
    }
}