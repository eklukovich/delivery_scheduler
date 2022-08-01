package com.eklukovich.deliveryscheduler.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AddressUtilsTest {

    @Test
    fun `parseStreetName - empty string`() {
        val result = AddressUtils.parseStreetName("")
        assertThat(result).isEmpty()
    }

    @Test
    fun `parseStreetName - only building number`() {
        val result = AddressUtils.parseStreetName("123")
        assertThat(result).isEmpty()
    }

    @Test
    fun `parseStreetName - building number and street name`() {
        val result = AddressUtils.parseStreetName("123 Main St")
        assertThat(result).isEqualTo("Main St")
    }

    @Test
    fun `parseStreetName - building number and street name and suite number`() {
        val result = AddressUtils.parseStreetName("123 Applwood Drive Suite 104")
        assertThat(result).isEqualTo("Applwood Drive")
    }

    @Test
    fun `parseStreetName - building number and street name and apt number`() {
        val result = AddressUtils.parseStreetName("213 Wagon Wheel Ct Apt 237")
        assertThat(result).isEqualTo("Wagon Wheel Ct")
    }

    @Test
    fun `parseStreetName - building number and apt number`() {
        val result = AddressUtils.parseStreetName("213 Apt 237")
        assertThat(result).isEmpty()
    }

    @Test
    fun `parseStreetName - long street name`() {
        val result = AddressUtils.parseStreetName("Really Long Named Street")
        assertThat(result).isEqualTo("Really Long Named Street")
    }
}