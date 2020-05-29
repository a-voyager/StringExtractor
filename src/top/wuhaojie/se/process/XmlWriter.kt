package top.wuhaojie.se.process

import top.wuhaojie.se.entity.TaskHolder

class XmlWriter : AbsWriter() {

    override fun process(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        for (field in taskHolder.selectedFields()) {
            val text = field.source
            content = content.replace("android:text=\"$text\"", "android:text=\"@string/${field.result}\"")
        }

        writeFileContent(file, content)
    }

}