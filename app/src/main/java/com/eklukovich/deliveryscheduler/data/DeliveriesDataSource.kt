package com.eklukovich.deliveryscheduler.data

import com.eklukovich.deliveryscheduler.data.common.DataSource
import com.eklukovich.deliveryscheduler.data.model.DeliveriesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DeliveriesDataSource: DataSource<DeliveriesResponse> {

    // Hardcoding, but this would be fetched from an API
    private val jsonData = """
        {
          "shipments": [
            "215 Osinski Manors",
            "9856 Marvin Stravenue",
            "7127 Kathlyn Ferry",
            "987 Champlin Lake",
            "63187 Volkman Garden Suite 447",
            "75855 Dessie Lights",
            "1797 Adolf Island Apt. 744",
            "2431 Lindgren Corners",
            "8725 Aufderhar River Suite 859",
            "79035 Shanna Light Apt. 322"
          ],
          "drivers": [
            "Everardo Welch",
            "Orval Mayert",
            "Howard Emmerich",
            "Izaiah Lowe",
            "Monica Hermann",
            "Ellis Wisozk",
            "Noemie Murphy",
            "Cleve Durgan",
            "Murphy Mosciski",
            "Kaiser Sose"
          ]
        }
    """.trimIndent()

    override fun fetchData(): Flow<DeliveriesResponse> {
        return flow {
            val deliveries = Json.decodeFromString<DeliveriesResponse>(jsonData)
            emit(deliveries)
        }
    }
}