package pl.karczewski.pogoda.data.model.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)