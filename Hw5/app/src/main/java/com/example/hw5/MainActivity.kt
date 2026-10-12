package com.example.hw5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = WizardPagerAdapter(this)
    }

    // 提供給 Fragment 呼叫切換頁面的方法
    fun navigateToTab(position: Int) {
        viewPager.currentItem = position
    }
}
