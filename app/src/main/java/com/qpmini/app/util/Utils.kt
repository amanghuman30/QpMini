package com.qpmini.app.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*


fun timeStampConversionToTime(timeStamp: Date): String? {
    val jdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    jdf.timeZone = TimeZone.getDefault()
    return jdf.format(timeStamp)
}

fun hideSoftKeyBoard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}