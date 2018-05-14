package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder

class XmlWriter : AbsWriter() {

    fun process(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        for (field in taskHolder.fields) {
            val text = field.source
            content = content.replace("android:text=\"$text\"", "android:text=\"@string/${field.result}\"")
        }

        writeFileContent(file, content)
    }

}