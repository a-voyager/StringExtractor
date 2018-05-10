package top.wuhaojie.se.translate

object Translator {

    private val appId = "20180502000152597"
    private val securityKey = "JcH2Q2NXL4AWLg_Nsxbi"
    private val api by lazy {
        TransApi(appId, securityKey)
    }

    fun toEnglish(src: String): String {
        val json = api.getTransResult(src, "auto", "en")
        val response = JsonUtils.fromJson(json, TransResponse::class.java)
        if (response.trans_result.isNotEmpty()) {
            return response.trans_result[0].dst
        }
        return ""
    }

}