package top.wuhaojie.se.process.finder

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiFile
import top.wuhaojie.se.common.StringUtils
import top.wuhaojie.se.config.Config
import top.wuhaojie.se.entity.FieldEntity
import top.wuhaojie.se.entity.StringContainerFileType
import top.wuhaojie.se.entity.TaskHolder
import java.io.BufferedReader
import java.io.InputStreamReader

abstract class AbsFieldFinder {

    fun find(psiFile: PsiFile): TaskHolder {
        // save before operation
        FileDocumentManager.getInstance().saveAllDocuments()

        val taskHolder = TaskHolder()

        val virtualFile = psiFile.virtualFile ?: return taskHolder
        val inputStream = virtualFile.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))

        val stringBuilder = StringBuilder()
        while (true) {
            val line = reader.readLine() ?: break
            stringBuilder.append(line).appendln()
        }
        reader.close()
        val content = stringBuilder.toString()

        val regex = Regex(regex())
        val result = regex.findAll(content)

        taskHolder.fields = result
                .map { it.value }
                .map {
                    val source = transformToString(it)
                    val checked = isDefaultChecked(source)
                    buildField(source, checked)
                }
                .filter { it.source.isNotBlank() }
                .toList()


        val type = StringContainerFileType.findByName(psiFile.fileType.name)
        val template = Config.getInstance().getTemplate(type)
        taskHolder.extractTemplate = if (StringUtils.isEmpty(template)) "getString(\$id, \$args)" else template

        return taskHolder
    }

    protected open fun buildField(source: String, checked: Boolean) = FieldEntity(source = source, result = "", isSelected = checked, originSource = source)

    abstract fun isDefaultChecked(it: String): Boolean

    abstract protected fun transformToString(it: String): String

    abstract protected fun regex(): String

}