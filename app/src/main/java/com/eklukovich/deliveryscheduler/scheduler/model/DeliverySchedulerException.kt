package com.eklukovich.deliveryscheduler.scheduler.model

import com.eklukovich.deliveryscheduler.scheduler.DeliveryScheduler

/**
 * Exception class thrown when the [DeliveryScheduler] encounters an error
 */
internal class DeliverySchedulerException(message: String) : IllegalStateException(message)