package com.example.myweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myweatherapp.ui.theme.MyWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.AsyncImage


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val weatherViewModel: WeatherViewModel = hiltViewModel()
                    WeatherScreen(viewModel = weatherViewModel)
                }
            }
        }

    }

    @Composable
    fun WeatherScreen(viewModel: WeatherViewModel) {
        val weatherState by viewModel.weatherState.collectAsState()

        when (weatherState) {
            is WeatherState.Loading -> {
//                CircularProgressIndicator()
            }
            is WeatherState.Success -> {
                val weather = (weatherState as WeatherState.Success).data
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    /*Text(text = "City: New Delhi")
                    Text(text = "Temperature: ${weather.main.temp}")*/
                    Text(text = "Weather App", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    WeatherCard(weather)
                }
            }
            is WeatherState.Error -> {
                Text(text = (weatherState as WeatherState.Error).message)
            }
        }
    }

    @Composable
    fun WeatherCard(weather: WeatherResponse) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display weather icon (URL format: "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png")
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png",
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Main weather info
                Text(text = "City: ${weather.name}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Temperature: ${weather.main.temp}°C", fontSize = 20.sp)
                Text(text = "Condition: ${weather.weather[0].description.capitalize()}", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(8.dp))

                // Additional details
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Humidity: ${weather.main.humidity}%")
                    Text(text = "Wind: ${weather.wind.speed} m/s")
                }

                // Display sunrise and sunset
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sunrise: ${convertUnixToTime(weather.sys.sunrise)}")
                Text(text = "Sunset: ${convertUnixToTime(weather.sys.sunset)}")
            }
        }
    }

    fun convertUnixToTime(unixTime: Long): String {
        val date = Date(unixTime * 1000)
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(date)
    }
}

