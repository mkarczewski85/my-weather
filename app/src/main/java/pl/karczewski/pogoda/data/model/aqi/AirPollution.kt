package pl.karczewski.pogoda.data.model.aqi

data class AirPollution (
    val components: Components,
    val dt: Int,
    val main: Main
)