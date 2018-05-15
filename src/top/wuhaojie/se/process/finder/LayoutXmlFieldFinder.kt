package top.wuhaojie.se.process.finder

class LayoutXmlFieldFinder : AbsFieldFinder() {

    override fun isDefaultChecked(it: String): Boolean {
        return !it.contains("@string/")
    }

    override fun transformToString(it: String): String {
        val result = it.replace("android:text=\"", "")
        return result.replace("\"", "")
    }

    override fun regex(): String = "android:text=\".*?\""

}