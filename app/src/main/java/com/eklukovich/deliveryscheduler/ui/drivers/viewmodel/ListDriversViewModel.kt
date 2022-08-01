package com.eklukovich.deliveryscheduler.ui.drivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eklukovich.deliveryscheduler.repository.DeliveriesRepository
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.scheduler.DeliveryScheduler
import com.eklukovich.deliveryscheduler.scheduler.HungarianAlgorithmDeliveryScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ListDriversViewModel(
    private val deliveriesRepository: DeliveriesRepository = DeliveriesRepository(),
    private val deliveryScheduler: DeliveryScheduler = HungarianAlgorithmDeliveryScheduler()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListDriversUiState>(ListDriversUiState.Loading)
    val uiState: StateFlow<ListDriversUiState> get() = _uiState

    init {
        // Automatically load data and observe for data changes
        loadAndObserveData()
    }

    fun onDriverSelected(driver: Driver) {
        val result = deliveryScheduler.getScheduleForDriver(driver) ?: return
        // TODO - replace with event
        println("FINDME - Driver: ${result.driver.name} | Shipment: ${result.shipment.address}")
    }

    private fun loadAndObserveData() {
        viewModelScope.launch {
            // Set loading state
            _uiState.value = ListDriversUiState.Loading

            // Fetch the deliveries
            deliveriesRepository.fetchUnscheduledDeliveries().collect { deliveries ->
                // Report error and bail
                if (deliveries == null) {
                    _uiState.value = ListDriversUiState.Error
                    return@collect
                }

                // Update state flow with the latest drivers
                _uiState.value = ListDriversUiState.Success(drivers = deliveries.drivers)

                // Schedule all the new deliveries
                deliveryScheduler.scheduleDeliveries(deliveries)
            }
        }
    }
}