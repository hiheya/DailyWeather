package work.icu007.dailyweather.logic.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import work.icu007.dailyweather.DailyWeatherApplication
import work.icu007.dailyweather.logic.model.PlaceResponse


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-15:45
 * E-mail: rookie_l@icu007.work
 */

/*
* 我们在searchPlaces()方法的上面声明了一个@GET注解，这样当调用searchPlaces()方法的时候，
* Retrofit就会自动发起一条GET请求，去访问@GET注解中配置的地址。
* 其中，搜索城市数据的API中只有query这个参数是需要动态指定的，我们使用@Query注解的方式来进行实现，
* 另外两个参数是不会变的，因此固定写在@GET注解中即可。
* -----------------------------------------------------------------------------------
* 另外，searchPlaces()方法的返回值被声明成了Call<PlaceResponse>，
* 这样Retrofit就会将服务器返回的JSON数据自动解析成PlaceResponse对象了。
* */
interface PlaceService {
    @GET("v2/place?token=${DailyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlace(@Query("query") query: String) : Call<PlaceResponse>
}