package com.ardgahgroup.usterka.helpers

import android.R
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils

class LoadingBarDialog(
    private var context: Context,
    private var rootLayout: ConstraintLayout,
    private var window: Window
) {

    private lateinit var layoutParams: ConstraintLayout.LayoutParams
    private lateinit var loadingContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    init {
        defineLayoutParams()
        setUpLoadingContainer()
        setUpProgressBar()
    }

    fun showLoadingCircle() {
        disableTouchInView()
        loadingContainer.visibility = View.VISIBLE
    }

    fun hideLoadingCircle() {
        enableTouchInView()
        loadingContainer.visibility = View.INVISIBLE
    }

    fun setTextViewText(string: String) {
        textView.text = string
    }

    private fun setUpLoadingContainer() {
        val color =
            ColorUtils.setAlphaComponent(ContextCompat.getColor(context, R.color.black), 127)
        loadingContainer = LinearLayout(context)
        loadingContainer.gravity = Gravity.CENTER
        loadingContainer.setBackgroundColor(color)
        loadingContainer.orientation = LinearLayout.VERTICAL
        loadingContainer.visibility = View.GONE
        rootLayout.addView(loadingContainer)
    }

    private fun setUpProgressBar() {
        progressBar = ProgressBar(context, null, R.attr.progressBarStyleLarge)
        loadingContainer.layoutParams = layoutParams
        loadingContainer.addView(progressBar)

        textView = TextView(context)
        textView.textSize = 18F
        textView.setTextColor(context.getColor(R.color.holo_blue_light))
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.typeface = Typeface.DEFAULT_BOLD;
        loadingContainer.addView(textView)
    }

    private fun disableTouchInView() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun enableTouchInView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun defineLayoutParams() {
        layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
        layoutParams.endToEnd = ConstraintSet.PARENT_ID
        layoutParams.startToStart = ConstraintSet.PARENT_ID
        layoutParams.topToTop = ConstraintSet.PARENT_ID
    }

}