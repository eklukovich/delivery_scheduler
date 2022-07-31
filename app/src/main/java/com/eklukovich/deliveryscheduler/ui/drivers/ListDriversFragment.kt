package com.eklukovich.deliveryscheduler.ui.drivers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eklukovich.deliveryscheduler.R

class ListDriversFragment : Fragment(R.layout.list_drivers_fragment) {

    companion object {
        fun newInstance() = ListDriversFragment()
    }

    private val viewModel: ListDriversViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}