package com.ardgahgroup.usterka.data.model.login

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserData(
    var Id: Int,
    var Department: String,
    var Role: String,
    var Token: String
)