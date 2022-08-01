package com.eklukovich.deliveryscheduler.util

/**
 * Returns the number of characters in the string, ignoring white space
 */
fun String.noWhitespaceLength(): Int {
    return count { !it.isWhitespace() }
}

/**
 * Returns the number of vowels in the calling string
 */
fun String.countVowels(): Int {
    return count {
        val lowerChar = it.lowercaseChar()
        lowerChar == 'a' || lowerChar == 'e' || lowerChar == 'i' || lowerChar == 'o' || lowerChar == 'u'
    }
}