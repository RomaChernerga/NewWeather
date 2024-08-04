package com.example.newweatherapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragActivity : FragmentActivity, private val list: List<Fragment> ) : FragmentStateAdapter(fragActivity) {
    override fun getItemCount() =  list.size


    override fun createFragment(position: Int): Fragment = list[position]

}