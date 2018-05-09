package top.wuhaojie.se.ui;

import cn.vearn.checktreetable.FiledTreeTableModel;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.ux.CheckTreeTableManager;
import top.wuhaojie.se.action.DataWriter;
import top.wuhaojie.se.entity.FieldEntity;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

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
    private ArrayList<DefaultMutableTreeTableNode> defaultMutableTreeTableNodeList;


    public FieldsDialog(PsiElementFactory factory,
                        PsiClass psiClass, PsiFile file, Project project) {
        this.factory = factory;
        this.file = file;
        this.project = project;
        this.psiClass = psiClass;
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
    }

    private void onOK() {
        this.setAlwaysOnTop(false);
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {

            @Override
            public void run() {
                DataWriter dataWriter = new DataWriter(file, project, psiClass);
                dataWriter.go();
            }
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
        for (int i = 0; i < 8; i++) {
            FieldEntity entity = new FieldEntity();
            entity.setFieldName("FieldName");
            entity.setGenerate(true);
            entity.setKey("Key");
            entity.setType("type");
            entity.setValue("value");

            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(entity);
            root.add(node);
            defaultMutableTreeTableNodeList.add(node);
        }
    }

}
