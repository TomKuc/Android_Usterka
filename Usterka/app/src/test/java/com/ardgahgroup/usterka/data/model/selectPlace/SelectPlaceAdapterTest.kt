package com.ardgahgroup.usterka.data.model.selectPlace

import android.content.Context
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import kotlin.coroutines.coroutineContext

@RunWith(JUnit4::class)
class SelectPlaceAdapterTest {
    val activity = ActivityScenarioRule(UserMenuActivity::class.java)

    @Test
    fun AdapterSize() {
        val list = mutableListOf<SelectPlaceRowData>()
        list.add(SelectPlaceRowData("zgloszenie", 1, 0))
        list.add(SelectPlaceRowData("zgloszenie2", 2, 0))
        val context = mock(Context::class.java)
        val adapter = SelectPlaceAdapter(context, R.layout.list_place_view_element, list)
        val result = adapter.items.size
        assertEquals(result, 2)
    }
}