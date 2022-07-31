package com.eklukovich.deliveryscheduler.ui.drivers.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eklukovich.deliveryscheduler.domain.model.Driver

/**
 * Recycler adapter class to display a list of drivers
 */
internal class ListDriversAdapter(
    private val onDriverItemClicked: (Driver) -> Unit
): ListAdapter<Driver, DriverViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Driver>() {
            override fun areItemsTheSame(oldItem: Driver, newItem: Driver): Boolean {
                // Normally compare item ids or some unique value, but the driver's name should be sufficient
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Driver, newItem: Driver): Boolean {
                // Compare only the data we are going to display
                return oldItem.name == newItem.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        return DriverViewHolder(parent)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = currentList.getOrNull(position) ?: return

        holder.bind(
            driver = driver,
            onDriverItemClicked = onDriverItemClicked
        )
    }
}