package com.eklukovich.deliveryscheduler.ui.drivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eklukovich.deliveryscheduler.repository.DeliveriesRepository
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.scheduler.DeliveryScheduler
import com.eklukovich.deliveryscheduler.scheduler.HungarianAlgorithmDeliveryScheduler
import com.eklukovich.deliveryscheduler.scheduler.model.DeliverySchedulerException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ListDriversViewModel(
    private val deliveriesRepository: DeliveriesRepository = DeliveriesRepository(),
    private val deliveryScheduler: DeliveryScheduler = HungarianAlgorithmDeliveryScheduler()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListDriversUiState>(ListDriversUiState.Loading)
    val uiState: StateFlow<ListDriversUiState> get() = _uiState

    private val _event = MutableSharedFlow<ListDriversEvent>()
    val event: SharedFlow<ListDriversEvent> get() = _event

    init {
        // Automatically load data and observe for data changes
        loadAndObserveData()
    }

    fun onDriverSelected(driver: Driver) {
        val scheduledDelivery = deliveryScheduler.getScheduleForDriver(driver) ?: return
        val currentState = (_uiState.value as? ListDriversUiState.Success) ?: return

        // Update the driver list
        val updatedList = currentState.drivers.toMutableList()
            .map { driverUiState ->
                if(driverUiState.driver == driver) {
                    DriverListItemUiState(driver = driver, scheduledDelivery = scheduledDelivery)
                } else {
                    driverUiState
            }
        }

        _uiState.value = currentState.copy(drivers = updatedList)
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

                // Schedule all the new deliveries
                try {
                    deliveryScheduler.scheduleDeliveries(deliveries)
                } catch (ex: DeliverySchedulerException) {
                    _event.tryEmit(ListDriversEvent.SchedulingFailed)
                    _uiState.value = ListDriversUiState.Error
                    return@collect
                }

                // Update state flow with the latest drivers
                val listItems = deliveries.drivers.map { DriverListItemUiState(driver = it) }
                _uiState.value = ListDriversUiState.Success(drivers = listItems)
            }
        }
    }
}