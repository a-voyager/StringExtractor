import com.google.gson.Gson
import java.lang.reflect.Type

object JsonUtils {

    private val gson = Gson()

    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    fun <T> fromJson(json: String, typeOfT: Type): T {
        return gson.fromJson(json, typeOfT)
    }


    fun toJson(src: Any): String {
        return gson.toJson(src)
    }


}