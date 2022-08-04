package com.eklukovich.deliveryscheduler.scheduler

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SuitabilityScoreGeneratorTest {

    // System under test
    private lateinit var generator: SuitabilityScoreGenerator

    @Before
    fun setup() {
        generator = SuitabilityScoreGenerator()
    }

    @Test
    fun `empty input strings return 0`() {
        val result = generator.generate("", "")
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `even destination street name and no common factors`() {
        val result = generator.generate("Joker", "Gotham")
        assertThat(result).isEqualTo(3.0)
    }

    @Test
    fun `even destination street name and common factors`() {
        val result = generator.generate("The Riddler", "Gotham City")
        assertThat(result).isEqualTo(6.75)
    }

    @Test
    fun `odd destination street name and no common factors`() {
        val result = generator.generate("Batman", "Batcave")
        assertThat(result).isEqualTo(4.0)
    }

    @Test
    fun `odd destination street name and common factors`() {
        val result = generator.generate("Penguin", "Batcave")
        assertThat(result).isEqualTo(6.0)
    }
}