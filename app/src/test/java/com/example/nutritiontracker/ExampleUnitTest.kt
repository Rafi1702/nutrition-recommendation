package com.example.nutritiontracker

import org.junit.Test

import kotlin.reflect.full.functions
import com.example.nutritiontracker.AuthorizationPolicy.*
import com.example.nutritiontracker.ContentType.*
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
interface ExampleService{
    @CustomHeader([BEARER, API_KEY], contentType = JSON)
    fun example()
}

class ExampleUnitTest {
    @Test
    fun testReflectionCustomHeader() {
        val kClass = ExampleService::class

        for (function in kClass.functions) {
            val customHeader = function.annotations.find { it is CustomHeader } as? CustomHeader

            if (customHeader != null) {
                println("Fungsi: ${function.name}")
                for (policy in customHeader.authPolicy) {
                    println("- Policy: $policy")
                }
                println("- Content Type: ${customHeader.contentType}")
            }
        }
    }
}