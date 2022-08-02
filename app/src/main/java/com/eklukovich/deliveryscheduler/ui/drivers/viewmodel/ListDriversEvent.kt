package com.eklukovich.deliveryscheduler.ui.drivers.viewmodel

sealed class ListDriversEvent {

    object SchedulingFailed: ListDriversEvent()
}