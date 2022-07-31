package com.eklukovich.deliveryscheduler.ui.drivers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eklukovich.deliveryscheduler.domain.DeliveriesRepository
import kotlinx.coroutines.launch

internal class ListDriversViewModel(
    private val deliveriesRepository: DeliveriesRepository = DeliveriesRepository()
) : ViewModel() {

    fun loadData() {
        viewModelScope.launch {
            deliveriesRepository.fetchUnscheduledDeliveries().collect { deliveries ->
                Log.d("FINDME", "Drivers: ${deliveries.drivers.joinToString { it.name }}")
                Log.d("FINDME", "Shipments: ${deliveries.shipments.joinToString { it.address }}")
            }
        }
    }
}