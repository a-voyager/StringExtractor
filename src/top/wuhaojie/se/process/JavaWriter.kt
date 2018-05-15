package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder

class JavaWriter : AbsWriter() {


    fun process(taskHolder: TaskHolder) {
        write(taskHolder)
    }

    private fun write(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        for (field in taskHolder.selectedFields()) {
            val text = field.source
            content = content.replace("\"$text\"", "context.getString(R.id.${field.result})")
        }

        writeFileContent(file, content)
    }


}