package com.ssp.ddd.support.plugin.toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import com.ssp.ddd.support.plugin.factory.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * @author Gaofeicm
 */
public class MyToolWindow {

    private DefaultTableModel vObjListTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"序号", "表名", "类名", "操作"});
    private DefaultTableModel vObjTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"序号", "标签", "属性", "字段名/类名"});
    private JPanel listContent;
    private JLabel urlText;
    private JTextField url;
    private JButton loadButton;
    private JTable vObjListTable;
    private JScrollPane vObjListScrollPane;
    private JTable vObjTable;
    private JScrollPane vObjScrollPane;
    private JButton detailButton;

    public MyToolWindow(ToolWindow toolWindow) {
        loadButton.addActionListener(e -> this.initVObjList(url.getText()));
        detailButton.addActionListener(e -> this.initVObj());
    }

    public void initVObjList(String text) {
        int rowCount = vObjListTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            vObjListTableModel.removeRow(0);
        }
        if(!"".equals(text)){
            new VObjFactory(text);
            List<VObj> obj = VObjFactory.OBJ;
            for (int i = 0; i < obj.size(); i++) {
                VObj v = obj.get(i);
                vObjListTableModel.addRow(new Object[]{i + 1 + "", v.getTable(), v.getClazz(), "查看"});
            }
        }
        vObjListTable.setModel(vObjListTableModel);
        this.fitTableColumns(vObjListTable);
    }


    public void initVObj(){
        int selectedRow = vObjListTable.getSelectedRow();
        String text = vObjListTable.getValueAt(selectedRow, 2).toString();
        if(text == null) {
            return;
        }
        int rowCount = vObjTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            vObjTableModel.removeRow(0);
        }
        Optional<VObj> res = VObjFactory.OBJ.stream().filter(v -> v.getClazz().equals(text)).findFirst();
        if(res.isPresent()){
            VObj vObj = res.get();
            int num = 0;
            List<Property> properties = vObj.getProperties();
            for (int i = 0; i < properties.size(); i++) {
                Property property = properties.get(i);
                vObjTableModel.addRow(new String[]{++num + "", "property", property.getName(), property.getColumn()});
            }
            List<Join> joins = vObj.getJoins();
            for (int i = 0; i < joins.size(); i++) {
                Join join = joins.get(i);
                vObjTableModel.addRow(new String[]{++num + "", "join", join.getName(), join.getClazz()});
            }
            List<Ref> refs = vObj.getRefs();
            for (int i = 0; i < refs.size(); i++) {
                Ref ref = refs.get(i);
                vObjTableModel.addRow(new String[]{++num + "", "ref", ref.getName(), ref.getBean()});
            }
            Extend extend = vObj.getExtend();
            if (extend != null) {
                vObjTableModel.addRow(new String[]{++num + "", "extend", extend.getName(), extend.getClazz()});
            }
            vObjTable.setModel(vObjTableModel);
            this.fitTableColumns(vObjTable);
        }
    }

    public JPanel getListContent() {
        return listContent;
    }

    public class ButtonRenderer extends JButton implements TableCellRenderer {
        private String text;

        private ActionListener actionListener;

        public ButtonRenderer(String text, ActionListener actionListener){
            this.text = text;
            this.actionListener = actionListener;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(text);
            addActionListener(actionListener);
            return this;
        }
    }

    /**
     * 自适应列宽
     * @param myTable 表格对象
     */
    private void fitTableColumns(JTable myTable) {
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(myTable,column.getIdentifier(), false, false, -1, col)
                    .getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++){
                int preferredWidth = (int) myTable.getCellRenderer(row, col)
                        .getTableCellRendererComponent(myTable,myTable.getValueAt(row, col), false, false,row, col)
                        .getPreferredSize().getWidth();
                width = Math.max(width, preferredWidth);
            }
            header.setResizingColumn(column);
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }
}