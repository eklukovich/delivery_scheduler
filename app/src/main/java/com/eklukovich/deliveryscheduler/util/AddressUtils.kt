package com.eklukovich.deliveryscheduler.util

object AddressUtils {

    /**
     * Extracts the street name from the address string and removes the building and unit numbers:
     *  ex: 123 Main St -> Main St
     *      321 Applewood Drive Apt 203 -> Applewood Drive
     *
     * NOTE: currently only supports removing  "Suite" and "Apt" unit numbers
     */
    fun parseStreetName(address: String): String {
        // Get the index of the first letter, ie. ignore building number and white space
        val start = address.indexOfFirst { it.isLetter() }
            .takeIf { it != -1 }
            ?: return ""

        // Get the ending index of the street name and ignore anything after 'Suite' or 'Apt'
        // This should improve this by using a addressing parsing library that can work with real addresses
        val end = address.indexOfAny(strings = listOf("Suite", "Apt"), ignoreCase = true)
            .takeIf { it != -1 }
            ?: address.length

        return address.substring(start, end).trim()
    }
}