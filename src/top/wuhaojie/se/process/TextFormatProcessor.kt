package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder

object TextFormatProcessor {

    private val simpleWords = arrayOf(
            "a", "an", "the"
    )


    private fun removeSimpleWord(src: String): String {
        var result = src
        for (simpleWord in simpleWords) {
            result = result.replace(" $simpleWord ", " ")
        }
        return result
    }

    private fun cleanText(src: String): String {
        var result = src.replace(',', ' ')
        result = result.replace('.', ' ')
        result = result.replace('!', ' ')
        result = result.replace('?', ' ')
        result = result.replace('-', ' ')
        result = result.replace('/', ' ')
        result = result.replace(Regex(" +"), " ")
        result = result.trim()
        result = result.toLowerCase()
        return result
    }

    private fun concat(src: String): String {
        return src.replace(' ', '_')
    }


    fun processText(src: String): String {
        val removeSimpleWord = removeSimpleWord(src)
        val cleanText = cleanText(removeSimpleWord)
        return concat(cleanText)
    }


    fun process(taskHolder: TaskHolder) {
        val fields = taskHolder.selectedFields()
        for (i in IntRange(0, fields.size - 1)) {
            val src = fields[i].result
            val processText = processText(src)
            fields[i].result = processText
            fields[i].resultSrc = processText
        }
    }

}