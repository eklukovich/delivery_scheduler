package com.eklukovich.deliveryscheduler.data.common

import kotlinx.coroutines.flow.Flow

/**
 * Interface that defines a source of data to be used in the repository.
 * This can come any source, such as the network, local database, etc.
 */
interface DataSource<Result> {

    /**
     * Executes the data fetching functionality and returns the results as a [Flow]
     *
     * @return flow of results or null if an error was encountered
     */
    fun fetchData(): Flow<Result?>
}