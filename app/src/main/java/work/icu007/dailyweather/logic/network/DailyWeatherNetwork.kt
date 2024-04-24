package work.icu007.dailyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-16:18
 * E-mail: rookie_l@icu007.work
 */

object DailyWeatherNetwork {

    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    suspend fun getHourlyWeather(lng: String, lat: String) =
        weatherService.getHourlyWeather(lng, lat).await()

    // 这里使用ServiceCreator创建了一个PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    // 定义了 一个searchPlaces()函数，
    // 并在这里调用刚刚在PlaceService接口中定义的searchPlaces()方法，以发起搜索城市数据请求。
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    // `suspendCoroutine`函数必须在协程作用域或挂起函数中才能调用，它接收一个Lambda表达式参数，
    // 主要作用是将当前协程立即挂起，然后在一个普通的线程中执行Lambda表达式中的代码。
    // Lambda表达式的参数列表上会传入一个Continuation参数，
    // 调用它的`resume()`方法或`resumeWithException()`可以让协程恢复执行。

    /*
    * `await()`函数仍然是一个挂起函数，然后我们给它声明了一个泛型T，
    * 并将`await()`函数定义成了`Call<T>`的扩展函数，这样所有返回值
    * 是Call类型的`Retrofit`网络请求接口就都可以直接调用`await()`函数了。
    * */
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}