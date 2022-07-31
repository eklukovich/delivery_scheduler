package com.eklukovich.deliveryscheduler.ui.drivers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eklukovich.deliveryscheduler.databinding.DriverListItemBinding
import com.eklukovich.deliveryscheduler.repository.model.Driver

/**
 * Recycler ViewHolder class that displays a single driver
 */
internal class DriverViewHolder(
    parent: ViewGroup,
    private val binding: DriverListItemBinding = createBinding(parent)
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun createBinding(parent: ViewGroup): DriverListItemBinding {
            return DriverListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
    }

    fun bind(driver: Driver, onDriverItemClicked: (Driver) -> Unit) {
        // Set click listener
        itemView.setOnClickListener { onDriverItemClicked(driver) }

        // Set fields
        binding.driverNameText.text = driver.name
    }
}