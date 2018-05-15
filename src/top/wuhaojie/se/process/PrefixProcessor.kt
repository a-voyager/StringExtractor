package top.wuhaojie.se.process

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiFile
import top.wuhaojie.se.common.StringUtils
import top.wuhaojie.se.entity.TaskHolder

object PrefixProcessor {


    fun refreshDefaultPrefix(project: Project, psiFile: PsiFile, taskHolder: TaskHolder) {
        val builder = StringBuilder()
        val virtualFile = psiFile.virtualFile
        val module = ProjectRootManager.getInstance(project).fileIndex.getModuleForFile(virtualFile)
        val moduleName = if (module == null) "" else "${module.name.toLowerCase()}_"
        builder.append(moduleName)
        val name = virtualFile.name.split(".")[0]
        val componentName = "${formatComponentName(name)}_"
        builder.append(componentName)
        refreshPrefix(taskHolder, builder.toString())
    }

    private fun formatComponentName(name: String): String {
        return StringUtils.underscoreString(name)
    }

    fun refreshPrefix(taskHolder: TaskHolder, prefix: String) {
        taskHolder.prefix = prefix
        val fields = taskHolder.fields
        for (i in IntRange(0, fields.size - 1)) {
            fields[i].result = prefix + fields[i].resultSrc
        }
    }

}