package com.example.weatherpulse.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "locations_table")
data class LocationDTO(

    @PrimaryKey(autoGenerate = true)
    var id : Int,

    var name : String,
    var lat : Double,
    var lon : Double

) : Serializable {

}
