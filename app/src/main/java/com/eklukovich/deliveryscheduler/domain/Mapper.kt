package com.eklukovich.deliveryscheduler.domain

import com.eklukovich.deliveryscheduler.data.model.DeliveriesResponse
import com.eklukovich.deliveryscheduler.domain.model.Deliveries
import com.eklukovich.deliveryscheduler.domain.model.Driver
import com.eklukovich.deliveryscheduler.domain.model.Shipment

internal fun DeliveriesResponse.toDeliveries(): Deliveries {
    return Deliveries(
        drivers = drivers.map { Driver(name = it) },
        shipments = shipments.map { Shipment(address = it) }
    )
}