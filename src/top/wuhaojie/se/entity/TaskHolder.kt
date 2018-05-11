package top.wuhaojie.se.entity

data class TaskHolder(
        var prefix: String = "",
        var fields: List<FieldEntity> = emptyList()
) {
    fun selectedFields(): List<FieldEntity> {
        return fields.filter { it.isSelected }
    }

}