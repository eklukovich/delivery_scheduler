package com.eklukovich.deliveryscheduler.util

import com.eklukovich.deliveryscheduler.util.MathUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MathUtilsTest {

    @Test
    fun `findGCD - value of 0 and another number is the other number`() {
        val result = MathUtils.findGCD(0, 8)
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun `findGCD - value of 1 and another number is 1`() {
        val result = MathUtils.findGCD(1, 8)
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun `findGCD - same values`() {
        val result = MathUtils.findGCD(8, 8)
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun `findGCD - random values`() {
        val result = MathUtils.findGCD(54, 888)
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `findGCD - prime values`() {
        val result = MathUtils.findGCD(5, 13)
        assertThat(result).isEqualTo(1)
    }
}