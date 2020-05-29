package top.wuhaojie.se.process

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile
import top.wuhaojie.se.entity.TaskHolder
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

abstract open class AbsWriter {

    protected fun saveAllFile() {
        FileDocumentManager.getInstance().saveAllDocuments()
    }

    abstract fun process(taskHolder: TaskHolder)

    protected fun writeFileContent(file: VirtualFile, content: String) {
        val outputStream = file.getOutputStream(this)
        val writer = BufferedWriter(OutputStreamWriter(outputStream))
        writer.write(content)
        writer.close()
    }

    protected fun readFileContent(file: VirtualFile): String {
        val inputStream = file.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        val builder = StringBuilder()
        while (true) {
            line = reader.readLine() ?: break
            builder.append(line).appendln()
        }
        val content = builder.toString()
        reader.close()
        return content
    }

}