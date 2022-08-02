package com.eklukovich.deliveryscheduler.scheduler

import android.util.Log
import com.eklukovich.deliveryscheduler.repository.model.Deliveries
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.repository.model.Shipment
import com.eklukovich.deliveryscheduler.scheduler.model.DeliverySchedulerException
import com.eklukovich.deliveryscheduler.scheduler.model.ScheduledDelivery
import com.eklukovich.deliveryscheduler.util.AddressUtils
import org.jgrapht.alg.interfaces.MatchingAlgorithm
import org.jgrapht.alg.matching.KuhnMunkresMinimalWeightBipartitePerfectMatching
import org.jgrapht.generate.SimpleWeightedBipartiteGraphMatrixGenerator
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph

internal class HungarianAlgorithmDeliveryScheduler(
    private val suitabilityScoreGenerator: SuitabilityScoreGenerator = SuitabilityScoreGenerator()
): DeliveryScheduler {

    private val tag = HungarianAlgorithmDeliveryScheduler::class.java.simpleName

    private var driverToShipmentMap: Map<Driver, ScheduledDelivery> = emptyMap()

    @Throws(DeliverySchedulerException::class)
    override fun scheduleDeliveries(deliveries: Deliveries) {
        // Only support an equal number of drivers and shipments
        if (deliveries.drivers.size != deliveries.shipments.size) {
            Log.e(tag, "The number of drivers and shipments must be equal.")
            throw DeliverySchedulerException("The number of drivers and shipments must be equal.")
        }

        // Create the suitability matrix
        val suitabilityMatrix = generateSuitabilityMatrix(
            drivers = deliveries.drivers,
            shipments = deliveries.shipments
        )

        // Schedule all drivers and shipments
        val result = generateDeliverySchedules(
            drivers = deliveries.drivers,
            shipments = deliveries.shipments,
            suitabilityMatrix = suitabilityMatrix
        )

        // Create a map between the driver and the scheduled delivery
        driverToShipmentMap = createScheduledDriverMap(result)
    }

    override fun getScheduleForDriver(driver: Driver): ScheduledDelivery? {
        return driverToShipmentMap[driver]
    }

    private fun generateSuitabilityMatrix(
        drivers: List<Driver>,
        shipments: List<Shipment>
    ): Array<DoubleArray> {
        val size = drivers.size
        val matrix = Array(size) { DoubleArray(size) }
        var maxSuitability = 0.0

        // Calculate the suitability score for every driver and shipment
        drivers.forEachIndexed { driverIndex, driver ->
            shipments.forEachIndexed { shipmentIndex, shipment ->
                val suitability = suitabilityScoreGenerator.generate(
                    driverName = driver.name,
                    destinationStreetName = AddressUtils.parseStreetName(shipment.address)
                )
                maxSuitability = maxOf(suitability, maxSuitability)
                matrix[driverIndex][shipmentIndex] = suitability
            }
        }

        // Subtract the max value from each score.
        // We want the maximum score, but the Hungarian algorithm results in a minimized problem. The subtraction will covert our
        // maximize problem into a minimize problem and will give us the desired result
        matrix.forEachIndexed { row, array ->
            array.forEachIndexed { col, _ ->
                matrix[row][col] = maxSuitability - matrix[row][col]
            }
        }

        return matrix
    }

    private fun generateDeliverySchedules(
        drivers: List<Driver>,
        shipments: List<Shipment>,
        suitabilityMatrix: Array<DoubleArray>
    ): MatchingAlgorithm.Matching<Vertex, DefaultWeightedEdge> {
        val driverVertexList = drivers.map { Vertex.DriverVertex(it) }
        val shipmentVertexList = shipments.map { Vertex.ShipmentVertex(it) }

        // Create the graph
        val target = SimpleWeightedGraph<Vertex, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        val generator = SimpleWeightedBipartiteGraphMatrixGenerator<Vertex, DefaultWeightedEdge>()
            .first(driverVertexList)
            .second(shipmentVertexList)
            .weights(suitabilityMatrix)

        generator.generateGraph(target)

        return KuhnMunkresMinimalWeightBipartitePerfectMatching(
            target,
            driverVertexList.toSet(),
            shipmentVertexList.toSet()
        ).matching
    }

    private fun createScheduledDriverMap(result: MatchingAlgorithm.Matching<Vertex, DefaultWeightedEdge>): Map<Driver, ScheduledDelivery> {
        return buildMap {
            result.edges.forEach { edge ->

                val sourceVertex = result.graph.getEdgeSource(edge)
                val targetVertex = result.graph.getEdgeTarget(edge)

                val driver = (sourceVertex as? Vertex.DriverVertex)?.driver ?: (targetVertex as Vertex.DriverVertex).driver
                val shipment = (sourceVertex as? Vertex.ShipmentVertex)?.shipment ?: (targetVertex as Vertex.ShipmentVertex).shipment

                put(driver, ScheduledDelivery(driver, shipment))
            }
        }
    }

    private sealed class Vertex {
        data class ShipmentVertex(val shipment: Shipment): Vertex()
        data class DriverVertex(val driver: Driver): Vertex()
    }
}