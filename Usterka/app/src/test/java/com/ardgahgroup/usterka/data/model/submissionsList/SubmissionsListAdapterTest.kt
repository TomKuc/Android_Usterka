package com.ardgahgroup.usterka.data.model.submissionsList

import android.content.Context
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.model.selectPlace.SelectPlaceAdapter
import com.ardgahgroup.usterka.data.model.selectPlace.SelectPlaceRowData
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import org.junit.Assert.*

import org.junit.Test
import org.mockito.Mockito

class SubmissionsListAdapterTest {
    val activity = ActivityScenarioRule(UserMenuActivity::class.java)

    @Test
    fun AdapterSize() {
        val list = mutableListOf<SubmissionsListRowData>()
        list.add(SubmissionsListRowData("zgłoszenie", 0))
        list.add(SubmissionsListRowData("zgloszenie2", 1))
        val context = Mockito.mock(Context::class.java)
        val adapter = SubmissionsListAdapter(context, R.layout.list_place_view_element, list, "Zgłoszone")
        val result = adapter.items.size
        assertEquals(result, 2)
    }
}