package pl.karczewski.pogoda.data.model.geo

data class GeocodingDataModelItem(
    val country: String,
    val lat: Double,
    val local_names: Map<String, String>,
    val lon: Double,
    val name: String,
    val state: String
)