package com.eklukovich.deliveryscheduler.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eklukovich.deliveryscheduler.R
import com.eklukovich.deliveryscheduler.ui.drivers.ListDriversFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_fragment_container, ListDriversFragment.newInstance())
                    .commitNow()
        }
    }
}