package top.wuhaojie.se.process

class JavaFieldFinder : AbsFieldFinder() {

    override fun regex() = "\".*?\""

    override fun transformToString(it: String) = it.replace("\"", "")

}