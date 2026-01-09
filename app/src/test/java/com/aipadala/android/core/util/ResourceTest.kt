package com.aipadala.android.core.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceTest {

    @Test
    fun `Success resource returns correct data`() {
        val data = "test data"
        val resource = Resource.Success(data)

        assertTrue(resource.isSuccess())
        assertFalse(resource.isError())
        assertFalse(resource.isLoading())
        assertEquals(data, resource.getOrNull())
    }

    @Test
    fun `Error resource returns null for getOrNull`() {
        val resource = Resource.Error("Error message")

        assertFalse(resource.isSuccess())
        assertTrue(resource.isError())
        assertFalse(resource.isLoading())
        assertNull(resource.getOrNull())
    }

    @Test
    fun `Loading resource returns null for getOrNull`() {
        val resource = Resource.Loading

        assertFalse(resource.isSuccess())
        assertFalse(resource.isError())
        assertTrue(resource.isLoading())
        assertNull(resource.getOrNull())
    }

    @Test
    fun `getOrDefault returns data for Success`() {
        val data = "test data"
        val resource = Resource.Success(data)

        assertEquals(data, resource.getOrDefault("default"))
    }

    @Test
    fun `getOrDefault returns default for Error`() {
        val resource = Resource.Error("Error message")

        assertEquals("default", resource.getOrDefault("default"))
    }

    @Test
    fun `getOrDefault returns default for Loading`() {
        val resource: Resource<String> = Resource.Loading

        assertEquals("default", resource.getOrDefault("default"))
    }

    @Test
    fun `map transforms Success data`() {
        val resource = Resource.Success(5)
        val mapped = resource.map { it * 2 }

        assertTrue(mapped.isSuccess())
        assertEquals(10, mapped.getOrNull())
    }

    @Test
    fun `map preserves Error`() {
        val resource = Resource.Error("Error message", code = 404)
        val mapped = resource.map { "transformed" }

        assertTrue(mapped.isError())
        assertEquals("Error message", (mapped as Resource.Error).message)
        assertEquals(404, mapped.code)
    }

    @Test
    fun `map preserves Loading`() {
        val resource: Resource<Int> = Resource.Loading
        val mapped = resource.map { it * 2 }

        assertTrue(mapped.isLoading())
    }

    @Test
    fun `onSuccess executes action for Success`() {
        var actionCalled = false
        val resource = Resource.Success("data")

        resource.onSuccess { actionCalled = true }

        assertTrue(actionCalled)
    }

    @Test
    fun `onSuccess does not execute action for Error`() {
        var actionCalled = false
        val resource = Resource.Error("Error")

        resource.onSuccess { actionCalled = true }

        assertFalse(actionCalled)
    }

    @Test
    fun `onError executes action for Error`() {
        var errorMessage: String? = null
        val resource = Resource.Error("Error message")

        resource.onError { msg, _ -> errorMessage = msg }

        assertEquals("Error message", errorMessage)
    }

    @Test
    fun `onError does not execute action for Success`() {
        var actionCalled = false
        val resource = Resource.Success("data")

        resource.onError { _, _ -> actionCalled = true }

        assertFalse(actionCalled)
    }

    @Test
    fun `onLoading executes action for Loading`() {
        var actionCalled = false
        val resource: Resource<String> = Resource.Loading

        resource.onLoading { actionCalled = true }

        assertTrue(actionCalled)
    }

    @Test
    fun `companion factory methods create correct instances`() {
        val success = Resource.success("data")
        val error = Resource.error("message", code = 500)
        val loading = Resource.loading()

        assertTrue(success is Resource.Success)
        assertTrue(error is Resource.Error)
        assertTrue(loading is Resource.Loading)
    }

    @Test
    fun `Error contains throwable when provided`() {
        val exception = RuntimeException("Test exception")
        val resource = Resource.Error("Error", throwable = exception)

        assertEquals(exception, resource.throwable)
    }
}
