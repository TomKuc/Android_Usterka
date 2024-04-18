package com.ardgahgroup.usterka.data.model.submissionsList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ardgahgroup.usterka.R


class SubmissionsListAdapter(
    var myContext: Context,
    var resources: Int,
    var items: List<SubmissionsListRowData>,
    var myCategory: String?
) :
    ArrayAdapter<SubmissionsListRowData>(myContext, resources, items) {

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(myContext)
        val view: View = layoutInflater.inflate(resources, null)
        val mItem: SubmissionsListRowData = items[position]
        val textView = view.findViewById<TextView>(R.id.submission_item)
        textView.text = mItem.title

        when (myCategory) {
            myContext.getString(R.string.reported) -> {
                textView.setBackgroundResource(R.drawable.submissions_list_reported)
                textView.setTextColor(ContextCompat.getColor(myContext, R.color.list_item_reported))
            }
            myContext.getString(R.string.accepted) -> {
                textView.setBackgroundResource(R.drawable.submissions_list_accepted)
                textView.setTextColor(ContextCompat.getColor(myContext, R.color.list_item_accepted))
            }
            myContext.getString(R.string.in_progress) -> {
                textView.setBackgroundResource(R.drawable.submissions_list_in_progress)
                textView.setTextColor(
                    ContextCompat.getColor(
                        myContext,
                        R.color.list_item_in_progress
                    )
                )
            }
            myContext.getString(R.string.finished) -> {
                textView.setBackgroundResource(R.drawable.submissions_list_finished)
                textView.setTextColor(ContextCompat.getColor(myContext, R.color.list_item_finished))
            }
            myContext.getString(R.string.deleted) -> {
                textView.setBackgroundResource(R.drawable.submissions_list_deleted)
                textView.setTextColor(ContextCompat.getColor(myContext, R.color.list_item_deleted))
            }
            myContext.getString(R.string.suspended) -> {
                textView.setBackgroundResource(R.drawable.submissions_list_suspended)
                textView.setTextColor(
                    ContextCompat.getColor(
                        myContext,
                        R.color.list_item_suspended
                    )
                )
            }
            else -> {
                println("Can't chose color background")
            }
        }
        return view
    }
}