package com.eklukovich.deliveryscheduler.ui.drivers.viewmodel

import com.eklukovich.deliveryscheduler.repository.model.Driver

/**
 * Sealed class to model the possible UI states for the drivers list screen
 */
sealed class ListDriversUiState {
    data class Success(val drivers: List<Driver>): ListDriversUiState()
    object Loading: ListDriversUiState()
    object Error: ListDriversUiState()
}