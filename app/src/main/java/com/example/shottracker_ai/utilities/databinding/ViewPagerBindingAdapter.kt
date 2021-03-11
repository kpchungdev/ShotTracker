package com.example.shottracker_ai.utilities.databinding

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

object ViewPagerBindingAdapters {

    @JvmStatic
    @BindingAdapter("currentTab")
    fun setViewPagerCurrentTab(viewPager2: ViewPager2, currentTab: Int?) {
        if (currentTab!=null) {
            viewPager2.setCurrentItem(currentTab, true)
        }
    }

}