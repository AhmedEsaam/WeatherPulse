package com.example.weatherpulse.data

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class ForecastResponse (

    @SerializedName("cnt"     ) var cnt     : Int?            = null,
    @SerializedName("list"    ) var list    : List<WeatherDTO> = listOf(),
    @SerializedName("city"    ) var city    : City?           = City()
)


data class City (

    @SerializedName("id"         ) var id         : Int?    = null,
    @SerializedName("name"       ) var name       : String? = null,
    @Embedded("coord_"      ) var coord      : Coord?  = Coord(),
    @SerializedName("country"    ) var country    : String? = null,
    @SerializedName("population" ) var population : Int?    = null,
    @SerializedName("timezone"   ) var timezone   : Int?    = null,
    @SerializedName("sunrise"    ) var sunrise    : Int?    = null,
    @SerializedName("sunset"     ) var sunset     : Int?    = null

)