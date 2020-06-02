package top.wuhaojie.se.action;

import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import top.wuhaojie.se.config.Config;
import top.wuhaojie.se.entity.StringContainerFileType;
import top.wuhaojie.se.entity.TaskHolder;
import top.wuhaojie.se.process.AbsWriter;
import top.wuhaojie.se.process.JavaKotlinWriter;
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

        StringContainerFileType type = StringContainerFileType.Companion.findByName(file.getFileType().getName());

        AbsWriter writer = null;
        if (type == StringContainerFileType.JAVA || type == StringContainerFileType.KOTLIN) {
            writer = new JavaKotlinWriter();
        } else if (type == StringContainerFileType.XML) {
            writer = new XmlWriter();
        }

        if (writer == null) {
            return;
        }
        writer.process(taskHolder);

        StringsWriter stringsWriter = new StringsWriter(project);
        stringsWriter.process(taskHolder);

        Config.getInstance().putPrefix(taskHolder.getPrefix());
        Config.getInstance().putTemplate(type, taskHolder.getExtractTemplate());
        Config.getInstance().save();

    }

}
