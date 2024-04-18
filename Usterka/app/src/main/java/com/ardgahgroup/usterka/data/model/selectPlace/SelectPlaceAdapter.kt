package com.ardgahgroup.usterka.data.model.selectPlace

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ardgahgroup.usterka.R


class SelectPlaceAdapter(
    var myContext: Context,
    var resources: Int,
    var items: List<SelectPlaceRowData>
) :
    ArrayAdapter<SelectPlaceRowData>(myContext, resources, items) {
    lateinit var textView: TextView

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(myContext)
        val view: View = layoutInflater.inflate(resources, null)
        val mItem: SelectPlaceRowData = items[position]
        textView = view.findViewById<TextView>(R.id.place_item)
        textView.text = mItem.name
        return view
    }
}