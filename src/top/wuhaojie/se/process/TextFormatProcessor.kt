package top.wuhaojie.se.process

object TextFormatProcessor {

    private val simpleWords = arrayOf(
            "a", "an", "the"
    )


    private fun removeSimpleWord(src: String): String {
        var result = src
        for (simpleWord in simpleWords) {
            result = result.replace(" $simpleWord ", "")
        }
        return result
    }

    private fun cleanText(src: String): String {
        var result = src.replace(',', ' ')
        result = result.replace('.', ' ')
        result = result.replace('!', ' ')
        result = result.replace('?', ' ')
        result = result.replace('-', ' ')
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

}