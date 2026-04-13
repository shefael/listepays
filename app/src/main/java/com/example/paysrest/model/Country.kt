package com.example.paysrest.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name") val name: Name,
    @SerializedName("flags") val flags: Flag,
    @SerializedName("capital") val capital: List<String>?,
    @SerializedName("population") val population: Long,
    @SerializedName("continents") val continents: List<String>,
    @SerializedName("translations") val translations: Translations
)

data class Name(
    @SerializedName("common") val common: String
)

data class Flag(
    @SerializedName("png") val png: String
)

data class Translations(
    @SerializedName("fra") val fra: Translation
)

data class Translation(
    @SerializedName("common") val common: String
)
