package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder
import top.wuhaojie.se.process.finder.KotlinFieldFinder
import top.wuhaojie.se.translate.Translator

object TranslateProcessor {

    fun process(taskHolder: TaskHolder) {
        val fields = taskHolder.fields
        for (i in IntRange(0, fields.size - 1)) {
            val source = clearingSource(fields[i].originSource)
            val english = Translator.toEnglish(source)
            fields[i].result = english
            fields[i].resultSrc = english
        }
    }

    private fun clearingSource(source: String): String {
        var result = source
        result = result.replace(KotlinFieldFinder.ARG_FIELD_REGEX, "")
        result = result.replace("[\\pP‘’“”]", "")
        return result
    }

}