package work.icu007.dailyweather.logic.model

import com.google.gson.annotations.SerializedName


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-15:39
 * E-mail: rookie_l@icu007.work
 */

/*
* PlaceResponse.kt文件中定义的类与属性，完全就是按照搜索城市数据接口返回的JSON格式来定义的。
* 不过，由于JSON中一些字段的命名可能与Kotlin的命名规范不太一致，因此这里使用了@SerializedName注解的方式，
* 来让JSON字段和Kotlin字段之间建立映射关系。
* */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String, val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)