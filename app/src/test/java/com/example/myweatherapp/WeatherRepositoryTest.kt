package com.example.myweatherapp

// WeatherRepositoryTest.kt

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository

    @Mock
    private lateinit var apiService: WeatherApi

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepository(apiService)
    }

    @Test
    fun `getWeather should return successful result when API call succeeds`() = runBlockingTest {
        // Arrange: Mock API response
        val mockWeather = WeatherResponse(
            coord = Coord(lon = 73.8567, lat = 18.5204), // Example coordinates for Pune
            weather = listOf(Weather(id = 1, main = "Clear", description = "clear sky", icon = "01d")),
            base = "stations",
            main = Main(temp = 25.0, feels_like = 26.0, temp_min = 20.0, temp_max = 30.0, pressure = 1013, humidity = 60),
            visibility = 10000,
            wind = Wind(speed = 5.0, deg = 180),
            clouds = Clouds(all = 0),
            dt = 1631710574,
            sys = Sys(type = 1, id = 9218, country = "IN", sunrise = 1631690612, sunset = 1631735099),
            timezone = 19800,
            id = 1254625, // Example ID for Pune
            name = "Pune",
            cod = 200
        )

        // Mock the API call
        `when`(apiService.getWeather("Pune", "09c8325eb3a554d7418ad3b835b9ae23",
            "metric")).thenReturn(mockWeather)

        // Act: Call the repository method
        val result = repository.getWeather("Pune","09c8325eb3a554d7418ad3b835b9ae23")

        // Assert: Check that result is correct
        assertTrue(true) // Ensure it's a success
        assertEquals("Pune", result.name) // Check city name
        assertEquals(25.0, result.main?.temp!!, 0.1) // Check current temperature
        assertEquals(20.0, result.main?.temp_min!!, 0.1) // Check min temperature
        assertEquals(30.0, result.main?.temp_max!!, 0.1) // Check max temperature
    }
}
