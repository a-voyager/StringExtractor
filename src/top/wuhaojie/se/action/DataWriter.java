package top.wuhaojie.se.action;

import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import top.wuhaojie.se.entity.TaskHolder;
import top.wuhaojie.se.process.JavaWriter;
import top.wuhaojie.se.process.StringsWriter;
import top.wuhaojie.se.process.XmlWriter;
import top.wuhaojie.se.ui.Toast;

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
    private TaskHolder taskHolder;

    public DataWriter(PsiFile file, Project project, PsiClass cls, TaskHolder taskHolder) {
        super(project, file);
        factory = JavaPsiFacade.getElementFactory(project);
        this.file = file;
        this.project = project;
        this.cls = cls;
        this.taskHolder = taskHolder;
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
    protected void run() {

        if (file instanceof PsiJavaFile) {
            JavaWriter javaWriter = new JavaWriter();
            javaWriter.process(taskHolder);
        } else if (file instanceof XmlFile) {
            XmlWriter xmlWriter = new XmlWriter();
            xmlWriter.process(taskHolder);
        }

        StringsWriter stringsWriter = new StringsWriter(project);
        stringsWriter.process(taskHolder);


    }

}
