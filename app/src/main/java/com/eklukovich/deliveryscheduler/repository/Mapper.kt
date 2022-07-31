package com.eklukovich.deliveryscheduler.repository

import com.eklukovich.deliveryscheduler.data.model.DeliveriesResponse
import com.eklukovich.deliveryscheduler.repository.model.Deliveries
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.repository.model.Shipment

internal fun DeliveriesResponse.toDeliveries(): Deliveries {
    return Deliveries(
        drivers = drivers.map { Driver(name = it) },
        shipments = shipments.map { Shipment(address = it) }
    )
}