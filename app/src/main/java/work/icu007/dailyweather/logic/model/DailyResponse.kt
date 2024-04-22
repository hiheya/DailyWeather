package work.icu007.dailyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date


/*
 * Author: Charlie_Liam
 * Time: 2024/3/26-16:56
 * E-mail: rookie_l@icu007.work
 */

data class DailyResponse(val status: String, val result: Result) {

    data class LifeDescription(val desc: String)
    data class TimeDescription(val time: String)

    data class LifeIndex(
        val coldRisk: List<LifeDescription>,
        val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>,
        val dressing: List<LifeDescription>
    )

    data class Astro(
        val date: Date,
        val sunrise: TimeDescription,
        val sunset: TimeDescription
    )

    data class Skycon(val value: String, val date: Date)

    data class Temperature(val max: Float, val min: Float)

    data class Daily(
        val temperature: List<Temperature>,
        val skycon: List<Skycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex,
        val astro: List<Astro>
    )

    data class Result(val daily: Daily)
}