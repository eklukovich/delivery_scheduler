package com.eklukovich.deliveryscheduler.ui.drivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eklukovich.deliveryscheduler.domain.DeliveriesRepository
import com.eklukovich.deliveryscheduler.domain.model.Driver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ListDriversViewModel(
    private val deliveriesRepository: DeliveriesRepository = DeliveriesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListDriversUiState>(ListDriversUiState.Loading)
    val uiState: StateFlow<ListDriversUiState> get() = _uiState

    init {
        // Automatically load data and observe for data changes
        loadAndObserveData()
    }

    fun onDriverSelected(driver: Driver) {
        // TODO()
    }

    private fun loadAndObserveData() {
        viewModelScope.launch {
            // Set loading state
            _uiState.value = ListDriversUiState.Loading

            // Fetch the deliveries
            deliveriesRepository.fetchUnscheduledDeliveries().collect { deliveries ->
                // Update state flow with the latest drivers
                _uiState.value = when (deliveries) {
                    null -> ListDriversUiState.Error
                    else -> ListDriversUiState.Success(drivers = deliveries.drivers)
                }
            }
        }
    }
}