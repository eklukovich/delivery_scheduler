package com.eklukovich.deliveryscheduler.ui.drivers.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.DriverListItemUiState

/**
 * Recycler adapter class to display a list of drivers
 */
internal class ListDriversAdapter(
    private val onDriverItemClicked: (Driver) -> Unit
): ListAdapter<DriverListItemUiState, DriverViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DriverListItemUiState>() {
            override fun areItemsTheSame(oldItem: DriverListItemUiState, newItem: DriverListItemUiState): Boolean {
                // Normally compare item ids or some unique value, but the driver's name should be sufficient
                return oldItem.driver.name == newItem.driver.name
            }

            override fun areContentsTheSame(oldItem: DriverListItemUiState, newItem: DriverListItemUiState): Boolean {
                // Compare only the data we are going to display
                return oldItem.driver == newItem.driver && oldItem.scheduledDelivery == newItem.scheduledDelivery
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        return DriverViewHolder(parent)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val uiState = currentList.getOrNull(position) ?: return

        holder.bind(
            uiState = uiState,
            onDriverItemClicked = onDriverItemClicked
        )
    }
}