package com.eklukovich.deliveryscheduler.scheduler

import com.eklukovich.deliveryscheduler.repository.model.Deliveries
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.scheduler.model.ScheduledDelivery
import com.eklukovich.deliveryscheduler.util.AddressUtils
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching
import org.jgrapht.generate.SimpleWeightedBipartiteGraphMatrixGenerator
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph

internal class HungarianAlgorithmDeliveryScheduler(
    private val suitabilityScoreGenerator: SuitabilityScoreGenerator = SuitabilityScoreGenerator()
): DeliveryScheduler {

    private var driverToShipmentMap: Map<Driver, ScheduledDelivery> = emptyMap()

    override fun scheduleDeliveries(deliveries: Deliveries) {
        val driverList = deliveries.drivers.map { it.name }
        val shipmentList = deliveries.shipments.map { it.address }
        val costMatrix = generateCostMatrix(deliveries)

        // Create the graph
        val target = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        val generator = SimpleWeightedBipartiteGraphMatrixGenerator<String, DefaultWeightedEdge>()
            .first(driverList)
            .second(shipmentList)
            .weights(costMatrix)

        generator.generateGraph(target)

        val result = KuhnMunkresMinimalWeightBipartitePerfectMatching(target, driverList.toSet(), shipmentList.toSet()).matching

        driverToShipmentMap = buildMap {
            result.edges.forEach { edge ->
                val driver = deliveries.drivers.first { it.name == result.graph.getEdgeSource(edge) }
                val shipment = deliveries.shipments.first { it.address == result.graph.getEdgeTarget(edge)}

                put(driver, ScheduledDelivery(driver, shipment))
            }
        }
    }

    override fun getScheduleForDriver(driver: Driver): ScheduledDelivery? {
        return driverToShipmentMap[driver]
    }

    private fun generateCostMatrix(deliveries: Deliveries): Array<DoubleArray> {
        assert(deliveries.drivers.size == deliveries.shipments.size)
        val size = deliveries.drivers.size

        val matrix = Array(size) { DoubleArray(size) }

        deliveries.drivers.forEachIndexed { driverIndex, driver ->
            deliveries.shipments.forEachIndexed { shipmentIndex, shipment ->
                matrix[driverIndex][shipmentIndex] = suitabilityScoreGenerator.generate(
                    driverName = driver.name,
                    destinationStreetName = AddressUtils.parseStreetName(shipment.address)
                )
            }
        }

        // TODO need to subtract the highest cost to get maximum

        return matrix
    }
}