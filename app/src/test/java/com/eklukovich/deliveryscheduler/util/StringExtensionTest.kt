package com.eklukovich.deliveryscheduler.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtensionTest {

    // noWhitespaceLength() Tests

    @Test
    fun `noWhitespaceLength - empty string`() {
        val result = "".noWhitespaceLength()
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `noWhitespaceLength - no white space`() {
        val result = "Batman".noWhitespaceLength()
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `noWhitespaceLength - end white space`() {
        val result = "Batman   ".noWhitespaceLength()
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `noWhitespaceLength - middle white space`() {
        val result = "The Joker".noWhitespaceLength()
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun `noWhitespaceLength - only white space string`() {
        val result = "                ".noWhitespaceLength()
        'a'.isWhitespace()
        assertThat(result).isEqualTo(0)
    }

    // countVowels() Tests

    @Test
    fun `countVowels - empty string`() {
        val result = "".countVowels()
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `countVowels - all lower case vowels`() {
        val result = "The Joker".countVowels()
        assertThat(result).isEqualTo(3)
    }

    @Test
    fun `countVowels - mixed case vowels`() {
        val result = "Enter thE BatcAve".countVowels()
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `countVowels - no vowels`() {
        val result = "bcdfghjklmnpqrstvwxyz".countVowels()
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun `countVowels - alphabet returns correct vowels`() {
        val result = "abcdefghijklmnopqrstuvwxyz".countVowels()
        assertThat(result).isEqualTo(5)
    }
}