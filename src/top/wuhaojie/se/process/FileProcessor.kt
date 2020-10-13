package top.wuhaojie.se.process

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiFile
import top.wuhaojie.se.entity.TaskHolder
import java.io.File

object FileProcessor {


    // src/main/res/values/strings.xml
    private val defaultFilePath = "src/main/res/values/strings.xml"

    fun process(project: Project, psiFile: PsiFile, taskHolder: TaskHolder) {
        val virtualFile = psiFile.virtualFile
        taskHolder.currentFile = virtualFile
        val module = ProjectRootManager.getInstance(project).fileIndex.getModuleForFile(virtualFile) ?: return
        val absolutePath = File(ModuleRootManager.getInstance(module).contentRoots[0].path, defaultFilePath).absolutePath
        val desFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(absolutePath)
        // val moduleFile = module.moduleFile ?: return
        // val desFile = moduleFile.parent.findFileByRelativePath(defaultFilePath) ?: return
        taskHolder.desFile = desFile
    }

}