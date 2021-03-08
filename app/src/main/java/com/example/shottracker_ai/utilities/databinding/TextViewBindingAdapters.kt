package com.example.shottracker_ai.utilities.databinding

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object TextViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("textColorRes")
    fun setTextColorRes(textView: TextView, @ColorRes colorRes: Int?) {
        if (colorRes != null && colorRes != 0) {
            val color = ContextCompat.getColor(textView.context, colorRes)
            textView.setTextColor(color)
        }
    }

}