package com.example.weatherpulse.data


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherpulse.data.source.local.WeatherTypeConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Calendar
import java.util.Date

@Entity(tableName = "weather_table")
data class WeatherDTO (

    @PrimaryKey
    @SerializedName("dt"         ) var dt         : Int?               = null,

    var weather    : List<Weather>?     = emptyList(),
    @Embedded("coord_"     ) var coord      : Coord?             = Coord(),
    @Embedded("main_"      ) var main       : Main?              = Main(),
    @Embedded("wind_"      ) var wind       : Wind?              = Wind(),
    @Embedded("clouds_"    ) var clouds     : Clouds?            = Clouds(),
    @Embedded("sys_"       ) var sys        : Sys?               = Sys(),
    @SerializedName("timezone"   ) var timezone   : Int?               = null,
    @SerializedName("base"       ) var base       : String?            = null,
    @SerializedName("visibility" ) var visibility : Int?               = null,
    @SerializedName("id"         ) var id         : Int?               = null,
    @SerializedName("name"       ) var name       : String?            = null,
    @SerializedName("cod"        ) var cod        : Int?               = null,
    @SerializedName("pop"        ) var pop        : Int?               = null,
    @SerializedName("dt_txt"     ) var dtTxt      : String?            = null

) : Serializable {

    private val date: Date
        get() = dt?.let { Date(it * 1000L) } ?: Date()

    val day: Int
        get() = date.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.DAY_OF_MONTH)
        }

    val month: Int
        get() = date.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.MONTH) + 1
        }

    val year: Int
        get() = date.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.YEAR)
        }

    val hour: Int
        get() = date.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format
        }

    val minute: Int
        get() = date.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.MINUTE)
        }
}

data class Coord (

    @SerializedName("lon" ) var lon : Double? = null,
    @SerializedName("lat" ) var lat : Double? = null

)

data class Weather (

    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("main"        ) var main        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("icon"        ) var icon        : String? = null

)

data class Main (

    @SerializedName("temp"       ) var temp      : Double? = null,
    @SerializedName("feels_like" ) var feelsLike : Double? = null,
    @SerializedName("temp_min"   ) var tempMin   : Double? = null,
    @SerializedName("temp_max"   ) var tempMax   : Double? = null,
    @SerializedName("pressure"   ) var pressure  : Int?    = null,
    @SerializedName("humidity"   ) var humidity  : Int?    = null,
    @SerializedName("sea_level"  ) var seaLevel  : Int?    = null,
    @SerializedName("grnd_level" ) var grndLevel : Int?    = null

) {
    val tempInt: Int
        get() = temp?.toInt() ?: 0

    val tempMinInt: Int
        get() = tempMin?.toInt() ?: 0

    val tempMaxInt: Int
        get() = tempMax?.toInt() ?: 0

    val feelsLikeInt: Int
        get() = feelsLike?.toInt() ?: 0
}

data class Wind (

    @SerializedName("speed" ) var speed : Double? = null,
    @SerializedName("deg"   ) var deg   : Int?    = null,
    @SerializedName("gust"  ) var gust  : Double? = null

)

data class Clouds (

    @SerializedName("all" ) var all : Int? = null

)

data class Sys (

    @SerializedName("country" ) var country : String? = null,
    @SerializedName("sunrise" ) var sunrise : Int?    = null,
    @SerializedName("sunset"  ) var sunset  : Int?    = null,
    @SerializedName("pod" ) var pod : String? = null

)