package top.wuhaojie.se.process

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.XmlElementFactory
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlComment
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import top.wuhaojie.se.entity.TaskHolder

class StringsWriter(
        private val project: Project
) {


    private fun saveAllFile() {
        FileDocumentManager.getInstance().saveAllDocuments()
    }

    private fun openStringsFile(): XmlFile? {
        val baseDir = project.baseDir ?: return null
        val virtualFile = baseDir.findFileByRelativePath("res/strings.xml") ?: return null
        return PsiManager.getInstance(project).findFile(virtualFile) as? XmlFile ?: return null
    }

    private fun writeComment(rootTag: XmlTag, text: String) {
        val factory = XmlElementFactory.getInstance(project)
        val container = factory.createTagFromText("<comment><!-- $text --></comment>", XMLLanguage.INSTANCE)
        val xmlComment = PsiTreeUtil.getChildOfType(container, XmlComment::class.java) ?: return
        rootTag.add(xmlComment)
    }

    private fun writeContent(rootTag: XmlTag, taskHolder: TaskHolder) {
        val fields = taskHolder.fields
        for (field in fields) {
            val childTag = rootTag.createChildTag("string", "", field.source, false)
            childTag.setAttribute("name", field.result)
            rootTag.add(childTag)
        }
    }


    fun process(taskHolder: TaskHolder) {
        saveAllFile()
        val xmlFile = openStringsFile() ?: return
        val rootTag = xmlFile.rootTag ?: return
        writeComment(rootTag, "开始")
        writeContent(rootTag, taskHolder)
        saveAllFile()
    }

}