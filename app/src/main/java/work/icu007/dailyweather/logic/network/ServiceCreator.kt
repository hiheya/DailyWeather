package work.icu007.dailyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-15:49
 * E-mail: rookie_l@icu007.work
 */

/*
* 这里我们使用object关键字让`ServiceCreator`成为了一个单例类，
* 并在它的内部定义了一个`BASE_URL`常量，用于指定`Retrofit`的根路径。
* 然后同样是在内部使用`Retrofit.Builder`构建一个`Retrofit`对象，
* 注意这些都是用private修饰符来声明的，相当于对于外部而言它们都是不可见的。
* -------------------------------------------------------------------
* 最后，我们提供了一个外部可见的`create()`方法，并接收一个Class类型的参数。
* 当在外部调用这个方法时，实际上就是调用了`Retrofit`对象的`create()`方法，
* 从而创建出相应Service接口的动态代理对象。
* */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}