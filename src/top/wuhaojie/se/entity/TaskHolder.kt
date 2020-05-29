package top.wuhaojie.se.entity

import com.intellij.openapi.vfs.VirtualFile

data class TaskHolder(
        var prefix: String = "",
        var fields: List<FieldEntity> = emptyList(),
        var currentFile: VirtualFile? = null,
        var desFile: VirtualFile? = null,
        var descTag: String = "START",
        var extractTemplate: String = ""
) {
    fun selectedFields(): List<FieldEntity> {
        return fields.filter { it.isSelected }
    }

    fun isCodeFile(): Boolean {
        val name = currentFile?.fileType?.name ?: return false
        val type = StringContainerFileType.findByName(name)
        return type == StringContainerFileType.KOTLIN || type == StringContainerFileType.JAVA
    }

}