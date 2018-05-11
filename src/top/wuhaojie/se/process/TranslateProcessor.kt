package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder
import top.wuhaojie.se.translate.Translator

object TranslateProcessor {

    fun process(taskHolder: TaskHolder) {
        val fields = taskHolder.fields
        for (i in IntRange(0, fields.size - 1)) {
            val source = fields[i].source
            val english = Translator.toEnglish(source)
            fields[i].result = english
            fields[i].resultSrc = english
        }
    }

}