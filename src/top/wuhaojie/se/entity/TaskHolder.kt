package top.wuhaojie.se.entity

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.vfs.VirtualFile

data class TaskHolder(
        var prefix: String = "",
        var fields: List<FieldEntity> = emptyList(),
        var currentFile: VirtualFile? = null,
        var desFile: VirtualFile? = null,
        var descTag: String = "START",
        var javaExtractTemplate: String = "getString(\$id)"
) {
    fun selectedFields(): List<FieldEntity> {
        return fields.filter { it.isSelected }
    }

    fun isJavaFile(): Boolean {
        return currentFile?.fileType is JavaFileType
    }

}