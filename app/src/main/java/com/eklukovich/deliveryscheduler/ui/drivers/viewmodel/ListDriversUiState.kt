package com.eklukovich.deliveryscheduler.ui.drivers.viewmodel

import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.scheduler.model.ScheduledDelivery

/**
 * Sealed class to model the possible UI states for the drivers list screen
 */
internal sealed class ListDriversUiState {
    data class Success(val drivers: List<DriverListItemUiState>): ListDriversUiState()
    object Loading: ListDriversUiState()
    object Error: ListDriversUiState()
}

internal data class DriverListItemUiState(
    val driver: Driver,
    val scheduledDelivery: ScheduledDelivery? = null
)