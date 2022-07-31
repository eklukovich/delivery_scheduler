package com.eklukovich.deliveryscheduler.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveriesResponse(
    @SerialName("shipments") val shipments: List<String>,
    @SerialName("drivers") val drivers: List<String>
)