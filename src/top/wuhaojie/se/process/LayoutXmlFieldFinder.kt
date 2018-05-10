package top.wuhaojie.se.process

class LayoutXmlFieldFinder : AbsFieldFinder() {

    override fun transformToString(it: String): String {
        val result = it.replace("android:text=\"", "")
        return result.replace("\"", "")
    }

    override fun regex(): String = "android:text=\".*?\""

}