package com.eklukovich.deliveryscheduler.scheduler

import com.eklukovich.deliveryscheduler.repository.model.Deliveries
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.scheduler.model.ScheduledDelivery

/**
 * Defines the required functionality to schedule delivery drivers to shipments that will have a maximized
 * suitability score
 */
internal interface DeliveryScheduler {

    /**
     * Method to schedule all the deliveries that is maximized for best suitability
     */
    fun scheduleDeliveries(deliveries: Deliveries)

    /**
     * Returns the scheduled delivery information for specified driver
     *
     * @return returns the scheduled delivery, or null if not found
     */
    fun getScheduleForDriver(driver: Driver): ScheduledDelivery?
}