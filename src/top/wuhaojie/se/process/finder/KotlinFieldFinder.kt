package top.wuhaojie.se.process.finder

import top.wuhaojie.se.entity.FieldEntity
import top.wuhaojie.se.utils.Log

class KotlinFieldFinder : JavaFieldFinder() {

    companion object {

        private const val TAG = "KotlinFieldFinder"

        // ${xxx} $xxx ${xxx.xx}
        // \$  ( [a-zA-Z_]+ )  |  ( [^\\]{   .*?  [^\\]} )
        val ARG_FIELD_REGEX = Regex("\\$([a-zA-Z_]+)|[^\\\\][{](.*?[^\\\\])}")

    }

    override fun buildField(source: String, checked: Boolean): FieldEntity {
        val allMatches = ARG_FIELD_REGEX.findAll(source)

        val argsList = arrayListOf<String>()

        for (matchResult in allMatches) {
            var arg = ""
            if (matchResult.groupValues[1].isNotBlank()) {
                arg = matchResult.groupValues[1]
            }
            if (matchResult.groupValues[2].isNotBlank()) {
                arg = matchResult.groupValues[2]
            }
            argsList.add(arg.trim())
        }

        Log.d(TAG, "field args: $argsList")


        var index = 1
        val replace = ARG_FIELD_REGEX.replace(source) {
            "%${index++}\$s"
        }


        Log.d(TAG, "field replacement: $replace")


        val field = super.buildField(source, checked)
        field.source = replace
        field.originSource = source
        field.args = argsList

        return field
    }

}