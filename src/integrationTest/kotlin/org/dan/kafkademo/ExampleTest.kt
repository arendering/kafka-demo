package org.dan.kafkademo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExampleTest {

    @Test
    fun testNumber() {
        val number = 5
        assertThat(number).isEqualTo(5)
    }
}