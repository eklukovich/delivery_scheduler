package com.eklukovich.deliveryscheduler.scheduler.model

import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.repository.model.Shipment
import com.eklukovich.deliveryscheduler.scheduler.DeliveryScheduler

/**
 * Result class from the [DeliveryScheduler] that contains the scheduled driver and shipment
 */
internal data class ScheduledDelivery(
    val driver: Driver,
    val shipment: Shipment
)