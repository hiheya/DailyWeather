package work.icu007.dailyweather.logic.model

import com.google.gson.annotations.SerializedName


/*
 * Author: Charlie_Liam
 * Time: 2024/3/26-16:40
 * E-mail: rookie_l@icu007.work
 */

data class RealtimeResponse(val status: String, val result: Result) {

    class Result(val realtime: Realtime)
    data class AQI(val chn: Float)

    data class AirQuality(val aqi: AQI)

    data class Realtime(
        val skycon: String,
        val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality
    )
}