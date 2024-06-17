package pl.karczewski.pogoda.data

import pl.karczewski.pogoda.R

val weatherConditionsGraphic = mapOf(
    "01d" to R.drawable.wi_day_sunny,
    "01n" to R.drawable.wi_night_clear,
    "02d" to R.drawable.wi_day_cloudy,
    "02n" to R.drawable.wi_night_cloudy,
    "03d" to R.drawable.wi_cloud,
    "03n" to R.drawable.wi_day_cloudy_high,
    "04d" to R.drawable.wi_day_sunny_overcast,
    "04n" to R.drawable.wi_night_partly_cloudy,
    "09d" to R.drawable.wi_day_showers,
    "09n" to R.drawable.wi_day_rain,
    "10d" to R.drawable.wi_day_rain,
    "10n" to R.drawable.wi_night_rain,
    "11d" to R.drawable.wi_day_thunderstorm,
    "11n" to R.drawable.wi_night_thunderstorm,
    "13d" to R.drawable.wi_snow,
    "13n" to R.drawable.wi_night_alt_snow,
    "50d" to R.drawable.wi_fog,
    "50n" to R.drawable.wi_night_fog
)

val airQualityIndexLabels = mapOf(
    1 to "bardzo dobra",
    2 to "dobra",
    3 to "umiarkowana",
    4 to "zła",
    5 to "bardzo zła"
)

val pm10Norms = mapOf(
    1 to 20,
    2 to 50,
    3 to 100,
    4 to 200
)

val pm2_5Norms = mapOf(
    1 to 10,
    2 to 25,
    3 to 50,
    4 to 75
)

val so2Norms = mapOf(
    1 to 20,
    2 to 80,
    3 to 250,
    4 to 350
)

val no2Norms = mapOf(
    1 to 40,
    2 to 70,
    3 to 150,
    4 to 200
)

val o3Norms = mapOf(
    1 to 60,
    2 to 100,
    3 to 140,
    4 to 180
)

val coNorms = mapOf(
    1 to 4400,
    2 to 9400,
    3 to 12400,
    4 to 15400
)

fun pollutantIndexLabel(value: Double, norm: Map<Int, Int>): String {
    for (i in 1 until 5) {
        if (value <= norm[i]!!) {
            return airQualityIndexLabels[i]!!
        }
    }
    return airQualityIndexLabels[5]!!
}
