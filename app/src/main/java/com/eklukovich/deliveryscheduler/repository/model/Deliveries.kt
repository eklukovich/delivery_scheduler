package com.eklukovich.deliveryscheduler.repository.model

/**
 * Domain model that contains the data for unscheduled deliveries
 */
data class Deliveries(
    val drivers: List<Driver>,
    val shipments: List<Shipment>
)
