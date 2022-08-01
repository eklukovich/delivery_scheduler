package com.eklukovich.deliveryscheduler.util

object MathUtils {
    /**
     * Finds the greatest common divisor between two numbers using the Euclidean Algorithm.
     */
    fun findGCD(a: Int, b: Int): Int {
        if(a == 0) return b
        return findGCD(b % a, a)
    }
}