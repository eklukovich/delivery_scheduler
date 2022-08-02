package com.eklukovich.deliveryscheduler.viewmodel

import app.cash.turbine.test
import com.eklukovich.deliveryscheduler.coroutine.MainDispatcherRule
import com.eklukovich.deliveryscheduler.repository.DeliveriesRepository
import com.eklukovich.deliveryscheduler.repository.model.Deliveries
import com.eklukovich.deliveryscheduler.repository.model.Driver
import com.eklukovich.deliveryscheduler.repository.model.Shipment
import com.eklukovich.deliveryscheduler.scheduler.DeliveryScheduler
import com.eklukovich.deliveryscheduler.scheduler.model.DeliverySchedulerException
import com.eklukovich.deliveryscheduler.scheduler.model.ScheduledDelivery
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.DriverListItemUiState
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.ListDriversUiState
import com.eklukovich.deliveryscheduler.ui.drivers.viewmodel.ListDriversViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ListDriversViewModelTest {

    // Rules
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private val mockDeliveryRepository: DeliveriesRepository = mock()
    private val mockDeliveryScheduler: DeliveryScheduler = mock()

    // Fixtures
    private val batman = Driver("Batman")
    private val joker = Driver("Joker")

    private val gotham = Shipment("Gotham")
    private val arkhamAsylum = Shipment("Arkham Asylum")

    private val drivers = listOf(batman, joker)
    private val shipments = listOf(gotham, arkhamAsylum)

    // System under test
    private lateinit var viewModel: ListDriversViewModel

    @Test
    fun `Successful UI state is returned when repository returns data`() = runTest {
        // Setup
        viewModel = createViewModel(deliveries = Deliveries(drivers, shipments))

        val expectedListItems = listOf(
            DriverListItemUiState(driver = batman),
            DriverListItemUiState(driver = joker)
        )

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Loading)
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Success(expectedListItems))
        }
    }

    @Test
    fun `Error UI state is returned when repository returns no data`() = runTest {
        // Setup
        viewModel = createViewModel(deliveries = null)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Loading)
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Error.LoadingFailed)
        }
    }

    @Test
    fun `Error UI state is returned when scheduler has an exception`() = runTest {
        // Setup
        viewModel = createViewModel(
            deliveries = Deliveries(drivers, shipments),
            schedulerException = DeliverySchedulerException("Failed")
        )

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Loading)
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Error.SchedulingFailed)
        }
    }

    @Test
    fun `onDriverSelected - updates ui state with new scheduled delivery`() = runTest {
        // Setup
        viewModel = createViewModel(
            deliveries = Deliveries(drivers, shipments),
            scheduledDelivery = ScheduledDelivery(batman, arkhamAsylum)
        )

        val expectedListItems = listOf(
            DriverListItemUiState(driver = batman),
            DriverListItemUiState(driver = joker)
        )
        val expectedUpdatedListItems = listOf(
            DriverListItemUiState(driver = batman, scheduledDelivery = ScheduledDelivery(batman, arkhamAsylum)),
            DriverListItemUiState(driver = joker)
        )

        viewModel.uiState.test {
            // Load Data
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Loading)
            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Success(expectedListItems))

            // Call select driver
            viewModel.onDriverSelected(driver = batman)

            assertThat(awaitItem()).isEqualTo(ListDriversUiState.Success(expectedUpdatedListItems))
        }
    }

    private fun createViewModel(
        deliveries: Deliveries? = null,
        scheduledDelivery: ScheduledDelivery? = null,
        schedulerException: DeliverySchedulerException? = null
    ): ListDriversViewModel {

        whenever(mockDeliveryRepository.fetchUnscheduledDeliveries()).thenReturn(flowOf(deliveries))

        schedulerException?.let {
            whenever(mockDeliveryScheduler.scheduleDeliveries(any())).thenThrow(it)
        }

        whenever(mockDeliveryScheduler.getScheduleForDriver(any())).thenReturn(scheduledDelivery)

        return ListDriversViewModel(
            deliveriesRepository = mockDeliveryRepository,
            deliveryScheduler = mockDeliveryScheduler
        )
    }
}