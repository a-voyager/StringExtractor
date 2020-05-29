package top.wuhaojie.se.process.finder

open class JavaFieldFinder : AbsFieldFinder() {

    override fun isDefaultChecked(it: String): Boolean {
        return it.isNotBlank()
    }

    override fun regex() = "\".*?\""

    override fun transformToString(it: String) = it.replace("\"", "")


    // 判断一个字符是否是中文
    private fun isChinese(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    private fun isChinese(str: String?): Boolean {
        if (str == null) return false
        str.toCharArray().forEach { if (isChinese(it)) return true }
        return false
    }
}