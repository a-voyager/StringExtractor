package top.wuhaojie.se;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.eclipse.jdt.internal.compiler.ProcessTaskManager;
import org.jetbrains.annotations.NotNull;
import top.wuhaojie.se.entity.StringContainerFileType;
import top.wuhaojie.se.entity.TaskHolder;
import top.wuhaojie.se.process.*;
import top.wuhaojie.se.process.finder.AbsFieldFinder;
import top.wuhaojie.se.process.finder.JavaFieldFinder;
import top.wuhaojie.se.process.finder.KotlinFieldFinder;
import top.wuhaojie.se.process.finder.LayoutXmlFieldFinder;
import top.wuhaojie.se.ui.FieldsDialog;
import top.wuhaojie.se.ui.Toast;
import top.wuhaojie.se.utils.Log;

public class MainAction extends BaseGenerateAction {

    @SuppressWarnings("unused")
    public MainAction() {
        super(null);
    }

    @SuppressWarnings("unused")
    public MainAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForClass(final PsiClass targetClass) {
        return super.isValidForClass(targetClass);
    }

    @Override
    public boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return true;
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (project == null || editor == null) {
            return;
        }
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (psiFile == null) {
            return;
        }
        PsiClass psiClass = getTargetClass(editor, psiFile);
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

        AbsFieldFinder fieldFinder;
        FileType fileType = psiFile.getFileType();


        Log.d("当前文件类型", fileType.getName(), fileType.getDefaultExtension());

        String fileTypeName = fileType.getName();

        if (fileTypeName.isEmpty()) {
            return;
        }


        StringContainerFileType type = StringContainerFileType.Companion.findByName(fileTypeName);
        switch (type) {
            case KOTLIN:
                fieldFinder = new KotlinFieldFinder();
                break;
            case JAVA:
                fieldFinder = new JavaFieldFinder();
                break;
            case XML:
                fieldFinder = new LayoutXmlFieldFinder();
                break;
            default:
                return;
        }

//
//        ModuleManager instance = ModuleManager.getInstance(project);
//        Module[] modules = instance.getModules();

        TaskHolder taskHolder = fieldFinder.find(psiFile);

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "String Extractor: translating") {

            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setIndeterminate(true);

                FileProcessor.INSTANCE.process(project, psiFile, taskHolder);
                TranslateProcessor.INSTANCE.process(taskHolder);
                TextFormatProcessor.INSTANCE.process(taskHolder);
                PrefixProcessor.INSTANCE.refreshDefaultPrefix(project, psiFile, taskHolder);

                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {


                        FieldsDialog dialog = new FieldsDialog(factory, psiClass, psiFile, project, taskHolder);
                        dialog.setSize(800, 500);
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);

                    }
                });

                progressIndicator.setIndeterminate(false);
            }
        });


    }

}
