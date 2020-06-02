package top.wuhaojie.se.ui;

import cn.vearn.checktreetable.FiledTreeTableModel;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.ux.CheckTreeTableManager;
import top.wuhaojie.se.action.DataWriter;
import top.wuhaojie.se.entity.FieldEntity;
import top.wuhaojie.se.entity.TaskHolder;
import top.wuhaojie.se.process.JavaKotlinWriter;
import top.wuhaojie.se.process.PrefixProcessor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class FieldsDialog extends JFrame {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel filedPanel;
    private JScrollPane sp;
    private PsiClass psiClass;
    private PsiElementFactory factory;
    private PsiFile file;
    private Project project;
    private JLabel generateClass;
    private JTextField textPrefix;
    private JTextField etTemplate;
    private JLabel labelTemplate;
    private JLabel labelExample;
    private ArrayList<DefaultMutableTreeTableNode> defaultMutableTreeTableNodeList;

    private TaskHolder taskHolder;


    public FieldsDialog(PsiElementFactory factory, PsiClass psiClass, PsiFile file, Project project, TaskHolder taskHolder) {
        this.factory = factory;
        this.file = file;
        this.project = project;
        this.psiClass = psiClass;
        this.taskHolder = taskHolder;
        setContentPane(contentPane);
        setTitle("String Extractor");
        getRootPane().setDefaultButton(buttonOK);
        this.setAlwaysOnTop(true);
        initListener();
    }

    private void initListener() {
        defaultMutableTreeTableNodeList = new ArrayList<>();

        JXTreeTable jxTreeTable = new JXTreeTable(new FiledTreeTableModel(createData()));
        CheckTreeTableManager manager = new CheckTreeTableManager(jxTreeTable);
        manager.getSelectionModel().addPathsByNodes(defaultMutableTreeTableNodeList);
        jxTreeTable.getColumnModel().getColumn(0).setPreferredWidth(150);


        jxTreeTable.expandAll();
        jxTreeTable.setCellSelectionEnabled(false);
        final DefaultListSelectionModel defaultListSelectionModel = new DefaultListSelectionModel();
        jxTreeTable.setSelectionModel(defaultListSelectionModel);

        defaultListSelectionModel.setSelectionMode(SINGLE_SELECTION);
        defaultListSelectionModel.addListSelectionListener(e -> defaultListSelectionModel.clearSelection());
        defaultMutableTreeTableNodeList = null;
        jxTreeTable.setRowHeight(30);
        sp.setViewportView(jxTreeTable);


        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        textPrefix.setText(taskHolder.getPrefix());
        textPrefix.addActionListener(e -> {
            if (e.getID() == ActionEvent.ACTION_PERFORMED) {
                String text = e.getActionCommand();
                PrefixProcessor.INSTANCE.refreshPrefix(taskHolder, text);
                jxTreeTable.updateUI();
            }
        });
        textPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChanged();
            }

            private void onChanged() {
                String text = textPrefix.getText();
                PrefixProcessor.INSTANCE.refreshPrefix(taskHolder, text);
                jxTreeTable.updateUI();
            }
        });

        labelTemplate.setVisible(taskHolder.isCodeFile());
        etTemplate.setVisible(taskHolder.isCodeFile());
        labelExample.setVisible(taskHolder.isCodeFile());

        etTemplate.setText(taskHolder.getExtractTemplate());
        etTemplate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChanged();
            }

            private void onChanged() {
                checkTemplate();
            }
        });
        checkTemplate();
    }

    private void checkTemplate() {
        String text = etTemplate.getText();
        taskHolder.setExtractTemplate(text);
        taskHolder.setPrefix(textPrefix.getText());
        if (!JavaKotlinWriter.Companion.getRES_ID_REGEX().containsMatchIn(text)) {
            labelExample.setForeground(JBColor.RED);
            labelExample.setText("must contains \"$id\", eg: getString($id, $args)");
            return;
        }
        String templateEg = text;
        if (JavaKotlinWriter.Companion.getRES_ID_REGEX().containsMatchIn(templateEg)) {
            templateEg = JavaKotlinWriter.Companion.getRES_ID_REGEX().replace(templateEg, "R.string.simple_text");
        }
        if (JavaKotlinWriter.Companion.getARGS_REGEX().containsMatchIn(templateEg)) {
            templateEg = JavaKotlinWriter.Companion.replaceArgs(templateEg, "simpleField");
        }
        labelExample.setForeground(JBColor.GRAY);
        labelExample.setText("eg: " + templateEg);
    }

    private void onOK() {
        this.setAlwaysOnTop(false);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            setVisible(false);
            DataWriter dataWriter = new DataWriter(file, project, psiClass, taskHolder);
            dataWriter.go();
        });
    }

    private void onCancel() {
        dispose();
    }

    private DefaultMutableTreeTableNode createData() {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        createDataNode(root);
        return root;
    }

    private void createDataNode(DefaultMutableTreeTableNode root) {
        List<FieldEntity> fields = taskHolder.getFields();
        for (FieldEntity field : fields) {
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(field);
            root.add(node);
            defaultMutableTreeTableNodeList.add(node);
        }
    }

}
