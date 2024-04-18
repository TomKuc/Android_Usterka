package com.ardgahgroup.usterka.ui.activities.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.ardgahgroup.usterka.R
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {
    @Rule
    @JvmField

    val scenario: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun test() {
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(click())
    }
}