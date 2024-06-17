package pl.karczewski.pogoda.data.model.aqi

data class AirPollutionDataModel(
    val coord: Coord,
    val list: List<AirPollution>
)