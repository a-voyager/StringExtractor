package top.wuhaojie.se.action;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlComment;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import top.wuhaojie.se.ui.Toast;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: dim
 * Date: 14-7-4
 * Time: 下午3:58
 */
public class DataWriter extends WriteCommandAction.Simple {

    private PsiClass cls;
    private PsiElementFactory factory;
    private Project project;
    private PsiFile file;

    public DataWriter(PsiFile file, Project project, PsiClass cls) {
        super(project, file);
        factory = JavaPsiFacade.getElementFactory(project);
        this.file = file;
        this.project = project;
        this.cls = cls;
    }

    public void go() {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Extract String") {

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);
                long currentTimeMillis = System.currentTimeMillis();
                execute();
                progressIndicator.setIndeterminate(false);
                progressIndicator.setFraction(1.0);
                Toast.make(project, MessageType.INFO, "String Extracted [" + (System.currentTimeMillis() - currentTimeMillis) + " ms]\n");
            }
        });
    }

    @NotNull
    @Override
    public RunResult execute() {
        return super.execute();
    }

    @Override
    protected void run() throws Exception {
        FileDocumentManager.getInstance().saveAllDocuments();

        InputStream inputStream = file.getVirtualFile().getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line).append('\n');
        }
        String content = builder.toString();

        String newContent = content.replaceAll("(\".*?\")", "new HashMap().toString()");


        reader.close();


        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(cls);


        OutputStream outputStream = file.getVirtualFile().getOutputStream(this);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        writer.write(newContent);
        writer.close();


        VirtualFile workspaceFile = project.getBaseDir();
        if (workspaceFile != null) {
            VirtualFile stringsFile = workspaceFile.findFileByRelativePath("res/strings.xml");
            PsiFile file = PsiManager.getInstance(project).findFile(stringsFile);
            XmlFile xmlFile = (XmlFile) file;

            XmlTag rootTag = xmlFile.getRootTag();


            XmlElementFactory factory = XmlElementFactory.getInstance(project);
            final XmlTag element = factory.createTagFromText("<comment><!-- " + "注释" + " --></comment>", XMLLanguage.INSTANCE);
            final XmlComment newComment = PsiTreeUtil.getChildOfType(element, XmlComment.class);

            rootTag.add(newComment);

            for (int i = 0; i < 5; i++) {
                XmlTag childTag = rootTag.createChildTag("string", "", "value", false);
                childTag.setAttribute("name", "testtest");
                rootTag.add(childTag);
            }

        }

        FileDocumentManager.getInstance().saveAllDocuments();

    }

}
