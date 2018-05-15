package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder

class JavaWriter : AbsWriter() {


    fun process(taskHolder: TaskHolder) {
        write(taskHolder)
    }

    private fun write(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        val extractTemplate = taskHolder.javaExtractTemplate
        for (field in taskHolder.selectedFields()) {
            val text = field.source
            val replace = extractTemplate.replace("\$id", "R.id.${field.result}")
            content = content.replace("\"$text\"", replace)
        }

        writeFileContent(file, content)
    }


}