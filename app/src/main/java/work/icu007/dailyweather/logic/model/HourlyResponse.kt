package work.icu007.dailyweather.logic.model


/*
 * Author: Charlie Liao
 * Time: 2024/4/23-14:27
 * E-mail: charlie.liao@icu007.work
 */
data class HourlyResponse(val status: String, val result: Result) {
    /**
     * 天气
     * datetime:时间 value:天气现象
     */
    data class Skycon(val datetime: String, val value: String)

    /**
     * 温度
     * datetime:时间 value:温度
     */
    data class Temperature(val datetime: String, val value: Float)

    /**
     * 降水概率
     * datetime:时间 probability:降水概率
     */
    data class Precipitation(val datetime: String, val probability: Float)
    data class Hourly(
        val description: String,
        val datetime: String,
        val precipitation: List<Precipitation>,
        val temperature: List<Temperature>,
        val skycon: List<Skycon>
    );

    data class Result(val hourly: Hourly)
}



