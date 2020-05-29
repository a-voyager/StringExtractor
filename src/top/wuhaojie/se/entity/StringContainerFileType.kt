package top.wuhaojie.se.entity

enum class StringContainerFileType(val fileTypeName: String) {
    JAVA("java"),
    KOTLIN("kotlin"),
    XML("xml"),
    UNKNOWN("unknown");

    companion object {

        fun findByName(fileTypeName: String): StringContainerFileType {
            return values().find { it.fileTypeName == fileTypeName.toLowerCase() } ?: UNKNOWN
        }

    }


}