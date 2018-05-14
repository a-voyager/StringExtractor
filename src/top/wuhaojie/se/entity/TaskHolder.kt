package top.wuhaojie.se.entity

import com.intellij.openapi.vfs.VirtualFile

data class TaskHolder(
        var prefix: String = "",
        var fields: List<FieldEntity> = emptyList(),
        var currentFile: VirtualFile? = null,
        var desFile: VirtualFile? = null
) {
    fun selectedFields(): List<FieldEntity> {
        return fields.filter { it.isSelected }
    }

}