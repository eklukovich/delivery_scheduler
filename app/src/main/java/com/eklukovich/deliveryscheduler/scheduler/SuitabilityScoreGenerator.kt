package com.eklukovich.deliveryscheduler.scheduler

import com.eklukovich.deliveryscheduler.util.MathUtils
import com.eklukovich.deliveryscheduler.util.countVowels

import com.eklukovich.deliveryscheduler.util.noWhitespaceLength

/**
 * Class that defines the algorithm for generating a suitability score for a driver and a destination
 */
internal class SuitabilityScoreGenerator {

    /**
     * Returns the suitability score for the provided driver name and the destination street name
     *
     * @param driverName the name of the driver (accepts any case and white space)
     * @param destinationStreetName the name of street for the destination (accepts any case and white space)
     */
    fun generate(driverName: String, destinationStreetName: String): Double {
        val driversNameLength = driverName.noWhitespaceLength()
        val destinationStreetNameLength = destinationStreetName.noWhitespaceLength()

        // Count the number of values in the driver's name
        val driverNameVowels = driverName.countVowels()

        // Calculate the base score
        val score = if (destinationStreetNameLength.isEven()) {
            // Score is 1.5x the number of vowels in the driver’s name
            driverNameVowels * 1.5
        } else {
            // Score is number of consonants in the driver’s name
            (driversNameLength - driverNameVowels).toDouble()
        }

        // Score is increased by 50% if there are common factors besides 1
        return when (hasAnySharedCommonFactors(driversNameLength, destinationStreetNameLength)) {
            true -> score * 1.5
            else -> score
        }
    }

    private fun Int.isEven() = this % 2 == 0

    private fun hasAnySharedCommonFactors(a: Int, b: Int): Boolean {
        // Ignores a common factor of 1
        return MathUtils.findGCD(a, b) != 1
    }
}