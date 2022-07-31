package com.eklukovich.deliveryscheduler.domain

import com.eklukovich.deliveryscheduler.data.common.DataSource
import com.eklukovich.deliveryscheduler.data.DeliveriesDataSource
import com.eklukovich.deliveryscheduler.data.model.DeliveriesResponse
import com.eklukovich.deliveryscheduler.domain.model.Deliveries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository class responsible for fetching the list deliveries that need to be scheduled.
 */
class DeliveriesRepository(
    private val dataSource: DataSource<DeliveriesResponse> = DeliveriesDataSource()
) {

    fun fetchUnscheduledDeliveries(): Flow<Deliveries> {
        return dataSource.fetchData().map { it.toDeliveries() }
    }
}