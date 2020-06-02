package top.wuhaojie.se.process

import top.wuhaojie.se.entity.FieldEntity
import top.wuhaojie.se.entity.TaskHolder
import java.lang.StringBuilder

class JavaKotlinWriter : AbsWriter() {

    companion object {

        val ARGS_REGEX = Regex("(,?)\\s*[$]args")

        val RES_ID_REGEX = Regex("\\s*[$]id")

        fun replaceArgs(replacement: String, resourceArgs: String): String {
            var result = replacement
            result = result.replace(ARGS_REGEX) {
                val builder = StringBuilder()
                if (it.groupValues[1].isNotBlank()) {
                    builder.append(", ")
                }
                builder.append(resourceArgs)
                return@replace builder.toString()
            }
            return result
        }

    }

    override fun process(taskHolder: TaskHolder) {
        write(taskHolder)
    }

    private fun write(taskHolder: TaskHolder) {
        val file = taskHolder.currentFile ?: return
        var content = readFileContent(file)

        val extractTemplate = taskHolder.extractTemplate
        for (field in taskHolder.selectedFields()) {
            val text = field.originSource
            // 模板: "getString($id, $args)" "getString($id)"
            var replacement = extractTemplate
            // 资源 id: "R.string.value_id"
            val resourceId = "R.string.${field.result}"
            replacement = replacement.replace(RES_ID_REGEX, resourceId)
            // 参数: "title, subTitle, student.name, 10*5"
            if (replacement.contains(ARGS_REGEX) && field.args.isNotEmpty()) {
                val resourceArgs = buildArguments(field)
                replacement = replaceArgs(replacement, resourceArgs)
            }
            content = content.replace("\"$text\"", replacement)
        }

        writeFileContent(file, content)
    }

    private fun buildArguments(field: FieldEntity): String {
        return field.args.joinToString(separator = ", ")
    }


}