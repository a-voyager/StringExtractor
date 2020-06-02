package top.wuhaojie.se.process

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiManager
import com.intellij.psi.XmlElementFactory
import com.intellij.psi.impl.source.xml.XmlElementImpl
import com.intellij.psi.impl.source.xml.XmlTagImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.xml.*
import org.jetbrains.rpc.LOG
import top.wuhaojie.se.entity.TaskHolder
import top.wuhaojie.se.utils.Log

class StringsWriter(
        private val project: Project
) : AbsWriter() {

    private val stringResourceMap = hashMapOf<String, String>()

    private fun openStringsFile(taskHolder: TaskHolder): XmlFile? {
        val virtualFile = taskHolder.desFile ?: return null
        return PsiManager.getInstance(project).findFile(virtualFile) as? XmlFile ?: return null
    }

    private fun writeComment(rootTag: XmlTag, text: String) {
        val factory = XmlElementFactory.getInstance(project)
        val container = factory.createTagFromText("<comment><!-- $text --></comment>", XMLLanguage.INSTANCE)
        val xmlComment = PsiTreeUtil.getChildOfType(container, XmlComment::class.java) ?: return
        rootTag.add(xmlComment)
    }

    private fun writeContent(rootTag: XmlTag, taskHolder: TaskHolder) {
        val fields = taskHolder.selectedFields()
        for (field in fields) {
            val resKey = field.result
            val resValue = field.source
            if (stringResourceMap.containsKey(resKey) && stringResourceMap[resKey] == resValue) {
                Log.d("repeat string key and value ", resKey, resValue)
                return
            }
            stringResourceMap[resKey] = resValue
            val childTag = rootTag.createChildTag("string", "", resValue, false)
            childTag.setAttribute("name", resKey)
            rootTag.add(childTag)
        }
    }


    override fun process(taskHolder: TaskHolder) {
        if (taskHolder.selectedFields().isEmpty()) {
            return
        }
        saveAllFile()
        val xmlFile = openStringsFile(taskHolder) ?: return
        val rootTag = xmlFile.rootTag ?: return

        rootTag.acceptChildren(object : PsiElementVisitor() {

            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element.elementType != XmlElementType.XML_TAG) {
                    return
                }
                val xmlTag = element as XmlTag
                val attribute = xmlTag.attributes.find { it.name == "name" } ?: return
                val resKey = attribute.value.toString()
                val resValue = xmlTag.value.trimmedText
                stringResourceMap[resKey] = resValue
            }

        })

        Log.d("existed string", stringResourceMap)

//        writeComment(rootTag, taskHolder.descTag)
        writeContent(rootTag, taskHolder)
        saveAllFile()
    }

}