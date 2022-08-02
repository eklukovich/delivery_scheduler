package com.eklukovich.deliveryscheduler.scheduler

import com.eklukovich.deliveryscheduler.repository.model.Deliveries
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.repository.model.Shipment
import com.eklukovich.deliveryscheduler.scheduler.model.DeliverySchedulerException
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HungarianAlgorithmDeliverySchedulerTest {

    // Mocks
    private val mockSuitabilityScoreGenerator: SuitabilityScoreGenerator = mock()

    // Fixtures
    private val batman = Driver("Batman")
    private val joker = Driver("Joker")
    private val robin = Driver("Robin")


    private val gotham = Shipment("Gotham")
    private val arkhamAsylum = Shipment("Arkham Asylum")
    private val wayneManor = Shipment("Wayne Manor")

    // System under test
    private lateinit var scheduler: HungarianAlgorithmDeliveryScheduler

    @Test(expected = DeliverySchedulerException::class)
    fun `scheduleDeliveries - DeliverySchedulerException is thrown if number of drivers and shipments differ`() {
        scheduler = createScheduler()

        scheduler.scheduleDeliveries(
            deliveries = Deliveries(
                drivers = listOf(batman),
                shipments = listOf(gotham, arkhamAsylum)
            )
        )
    }

    @Test
    fun `scheduleDeliveries - properly schedules for maximum suitability`() {
        whenever(mockSuitabilityScoreGenerator.generate(eq(batman.name), eq(gotham.address))).thenReturn(20.0)
        whenever(mockSuitabilityScoreGenerator.generate(eq(batman.name), eq(arkhamAsylum.address))).thenReturn(4.0)
        whenever(mockSuitabilityScoreGenerator.generate(eq(batman.name), eq(wayneManor.address))).thenReturn(2.0)

        whenever(mockSuitabilityScoreGenerator.generate(eq(joker.name), eq(gotham.address))).thenReturn(6.0)
        whenever(mockSuitabilityScoreGenerator.generate(eq(joker.name), eq(arkhamAsylum.address))).thenReturn(1.0)
        whenever(mockSuitabilityScoreGenerator.generate(eq(joker.name), eq(wayneManor.address))).thenReturn(15.0)

        whenever(mockSuitabilityScoreGenerator.generate(eq(robin.name), eq(gotham.address))).thenReturn(4.0)
        whenever(mockSuitabilityScoreGenerator.generate(eq(robin.name), eq(arkhamAsylum.address))).thenReturn(10.0)
        whenever(mockSuitabilityScoreGenerator.generate(eq(robin.name), eq(wayneManor.address))).thenReturn(3.0)

        scheduler = createScheduler()

        scheduler.scheduleDeliveries(
            deliveries = Deliveries(
                drivers = listOf(batman, joker, robin),
                shipments = listOf(gotham, arkhamAsylum, wayneManor)
            )
        )

        // Assert
        assertThat(scheduler.getScheduleForDriver(batman)!!.shipment).isEqualTo(gotham)
        assertThat(scheduler.getScheduleForDriver(joker)!!.shipment).isEqualTo(wayneManor)
        assertThat(scheduler.getScheduleForDriver(robin)!!.shipment).isEqualTo(arkhamAsylum)
    }


    private fun createScheduler(): HungarianAlgorithmDeliveryScheduler {
        return HungarianAlgorithmDeliveryScheduler(
            suitabilityScoreGenerator = mockSuitabilityScoreGenerator
        )
    }
}