package com.eklukovich.deliveryscheduler.ui.drivers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eklukovich.deliveryscheduler.databinding.DriverListItemBinding
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.DriverListItemUiState

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

    fun bind(uiState: DriverListItemUiState, onDriverItemClicked: (Driver) -> Unit) {
        // Set click listener
        itemView.setOnClickListener { onDriverItemClicked(uiState.driver) }

        // Set fields
        binding.driverNameText.text = uiState.driver.name
        binding.driverShipmentAddressText.text = uiState.scheduledDelivery?.shipment?.address ?: "--"
        binding.driverScheduledIcon.isSelected = uiState.scheduledDelivery != null
    }
}