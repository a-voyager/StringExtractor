package top.wuhaojie.se.translate

data class TransResponse(
        var from: String = "",
        var to: String = "",
        var trans_result: List<TransResult> = emptyList()
)