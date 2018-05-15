/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.swingx.ux;

import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import top.wuhaojie.se.entity.FieldEntity;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author vearn
 */
public class CheckTreeCellProvider extends ComponentProvider<JPanel> {

    private CheckTreeSelectionModel selectionModel;
    private TristateCheckBox _checkBox = null;
    private JLabel _label = null;

    public CheckTreeCellProvider(CheckTreeSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
        _checkBox = new TristateCheckBox(); //  创建一个TristateCheckBox实例
        _checkBox.setOpaque(false); //  设置TristateCheckBox不绘制背景
        _label = new JLabel();  //  创建一个JLabel实例
    }

    @Override
    protected void format(CellContext arg0) {
        //  从CellContext获取tree中的文字和图标
        JTree tree = (JTree) arg0.getComponent();
        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) arg0.getValue();
        Object obj = node.getUserObject();
        if (obj instanceof FieldEntity) {
            _label.setText(((FieldEntity) obj).getSource());
            _checkBox.setSelector((FieldEntity) obj);
        }

//        _label.setIcon(arg0.getIcon());

        //  根据selectionModel中的状态来绘制TristateCheckBox的外观
        TreePath path = tree.getPathForRow(arg0.getRow());
        if (path != null) {
            if (selectionModel.isPathSelected(path, true)) {
                _checkBox.setState(Boolean.TRUE);
            } else if (selectionModel.isPartiallySelected(path)) {
                _checkBox.setState(null);   //  注意“部分选中”状态的API
            } else {
                _checkBox.setState(Boolean.FALSE);
            }
        }
        if (obj instanceof FieldEntity) {
            _checkBox.setState(((FieldEntity) obj).isSelected());
        }

        //  使用BorderLayout布局，依次放置TristateCheckBox和JLabel
        rendererComponent.setLayout(new BorderLayout());
        rendererComponent.add(_checkBox);
        rendererComponent.add(_label, BorderLayout.LINE_END);
    }

    @Override
    protected void configureState(CellContext arg0) {
    }

    /**
     * 初始化一个JPanel来放置TristateCheckBox和JLabel
     */
    @Override
    protected JPanel createRendererComponent() {
        JPanel panel = new JPanel();
        return panel;
    }
}