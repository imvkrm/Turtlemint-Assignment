package com.vikram.turtlemintassignment

import com.vikram.turtlemintassignment.other.formattedDate
import com.vikram.turtlemintassignment.other.smartTruncate
import org.junit.Test

import org.junit.Assert.*


class UtilTest {
    @Test
    fun smartTruncateTest() {
        val value = "a b cdefg".smartTruncate(2)
        assertEquals("a b...", value)
    }

    @Test
    fun formattedDateTest(date : String) {
        val value = formattedDate("2022-04-30T15:44:04Z")
        assertEquals("30-04-2022", value)
    }
}