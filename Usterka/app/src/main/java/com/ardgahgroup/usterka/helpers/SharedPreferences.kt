package com.ardgahgroup.usterka.helpers

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val PREFERENCE = "PREFERENCE"
    private val EMAIL = "EMAIL"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

    fun getSavedEmail(): String? {
        return preference.getString(EMAIL, "")
    }

    fun setEmail(emailData: String?) {
        val editor = preference.edit()
        editor.putString(EMAIL, emailData)
        editor.apply()
    }
}