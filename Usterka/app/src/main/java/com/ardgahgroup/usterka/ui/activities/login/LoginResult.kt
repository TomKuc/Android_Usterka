package com.ardgahgroup.usterka.ui.activities.login


/** Authentication result : success (user details) or error message. */
data class LoginResult(
    val success: Boolean = false,
    val error: Int? = null
)