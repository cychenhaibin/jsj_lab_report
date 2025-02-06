package view.mainView;

import handler.MainViewTableHandler;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

public class MainViewTable extends JTable {
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem copyNameMenItem = new JMenuItem("复制姓名");
    JMenuItem copyPhoneMenItem = new JMenuItem("复制电话");
    JMenuItem copyEmailMenItem = new JMenuItem("复制邮箱");
    JMenuItem copyAddressMenItem = new JMenuItem("复制地址");

    public MainViewTable() {

        JTableHeader tableHeader = getTableHeader();
        tableHeader.setFont(new Font(null, Font.BOLD, 16));
        tableHeader.setForeground(Color.RED);

        //设置表格体
        setFont(new Font(null, Font.PLAIN, 14));
        setForeground(Color.BLACK);
        setGridColor(Color.BLACK);
        setRowHeight(30);
        //设置多行选择
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        MainViewTableHandler mainViewTableHandler = new MainViewTableHandler(this, popupMenu);
        copyNameMenItem.addActionListener(mainViewTableHandler);
        copyPhoneMenItem.addActionListener(mainViewTableHandler);
        copyEmailMenItem.addActionListener(mainViewTableHandler);
        copyAddressMenItem.addActionListener(mainViewTableHandler);

        popupMenu.add(copyNameMenItem);
        popupMenu.add(copyPhoneMenItem);
        popupMenu.add(copyEmailMenItem);
        popupMenu.add(copyAddressMenItem);
        addMouseListener(mainViewTableHandler);
    }

    //设置表格渲染方式
    public void renderRule() {
        Vector<String> columns = MainViewTableModel.getColumns();
        MainViewCellRender render = new MainViewCellRender();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn column = getColumn(columns.get(i));
            column.setCellRenderer(render);
            if (i == 0) {
                column.setPreferredWidth(50);
                column.setMaxWidth(50);
                column.setResizable(false);
            } else {
                column.setMinWidth(100);
            }
        }
    }
}
