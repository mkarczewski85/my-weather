package pl.karczewski.pogoda

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.karczewski.pogoda.data.NetworkResponse
import pl.karczewski.pogoda.data.airQualityIndexLabels
import pl.karczewski.pogoda.data.coNorms
import pl.karczewski.pogoda.data.model.aqi.AirPollutionDataModel
import pl.karczewski.pogoda.data.model.weather.Coord
import pl.karczewski.pogoda.data.model.weather.CurrentWeatherDataModel
import pl.karczewski.pogoda.data.no2Norms
import pl.karczewski.pogoda.data.o3Norms
import pl.karczewski.pogoda.data.pm10Norms
import pl.karczewski.pogoda.data.pm2_5Norms
import pl.karczewski.pogoda.data.pollutantIndexLabel
import pl.karczewski.pogoda.data.so2Norms
import pl.karczewski.pogoda.data.weatherConditionsGraphic

@Composable
fun WeatherPage(weatherViewModel: WeatherViewModel, airQualityViewModel: AirQualityViewModel) {

    var city by remember {
        mutableStateOf("")
    }

    val weatherDataResult = weatherViewModel.weatherResult.observeAsState()

    val airQualityDataResult = airQualityViewModel.airQualityResult.observeAsState()

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = { Text(text = "Lokalizacja") })
            IconButton(onClick = {
                weatherViewModel.getWeatherData(city)
                airQualityViewModel.getAirQualityData(city)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Szukaj lokalizacji")
            }
        }

        when (val result = weatherDataResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> {}
        }

        when (val result = airQualityDataResult.value) {
            is NetworkResponse.Error -> {
                Log.d("Error: ", result.message)
            }
            NetworkResponse.Loading -> {

            }
            is NetworkResponse.Success -> {
                AirQualityCard(data = result.data)
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: CurrentWeatherDataModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Ikona lokalizacji",
                modifier = Modifier.size(40.dp)
                )
            Text(text = data.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.sys.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.main.temp.toInt()}°C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(
                id = weatherConditionsGraphic.getOrDefault(data.weather[0].icon, R.drawable.wi_day_sunny)
            ),
            contentDescription = "Aktualna aura pogodowa",
            modifier = Modifier.size(160.dp)
        )
        Text(
            text = data.weather[0].description,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal(
                        "Wilgotność",
                        "${data.main.humidity}%",
                        R.drawable.wi_humidity
                    )
                    WeatherKeyVal(
                        "Prędkość wiatru",
                        "${data.wind.speed.toInt()} km/h",
                        R.drawable.wi_windy
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal(
                        "Temp max",
                        "${data.main.temp_max.toInt()}°C",
                        R.drawable.wi_thermometer
                    )
                    WeatherKeyVal(
                        "Temp min",
                        "${data.main.temp_min.toInt()}°C",
                        R.drawable.wi_thermometer_exterior
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal(
                        "Ciśnienie",
                        "${data.main.pressure} hPa",
                        R.drawable.wi_barometer
                    )
                    WeatherKeyVal(
                        "Opady",
                        "${data.rain?.`1h` ?: 0.0} mm",
                        R.drawable.wi_rain_mix
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String, icon: Int) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                painter = painterResource(
                    id = icon
                ),
                contentDescription = key,
                modifier = Modifier.size(30.dp)
            )
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Row {
            Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
        }
    }
}

@Composable
fun AirQualityCard(data: AirPollutionDataModel) {
    Spacer(modifier = Modifier.height(16.dp))
    val pollutionDetails = data.list[0]
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = "Ogólna jakość powietrza: ${airQualityIndexLabels[pollutionDetails.main.aqi]}",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AirQualityKeyVal("PM10", pollutionDetails.components.pm10, pm10Norms)
                AirQualityKeyVal("PM2.5", pollutionDetails.components.pm2_5, pm2_5Norms)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AirQualityKeyVal("SO2", pollutionDetails.components.so2, so2Norms)
                AirQualityKeyVal("NO2", pollutionDetails.components.no2, no2Norms)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AirQualityKeyVal("O3", pollutionDetails.components.o3, o3Norms)
                AirQualityKeyVal("CO", pollutionDetails.components.co, coNorms)
            }
        }
    }
}

@Composable
fun AirQualityKeyVal(key: String, value: Double, dict: Map<Int, Int>) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${key}: $value", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = pollutantIndexLabel(value, dict), fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}