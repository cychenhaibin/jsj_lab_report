package view.mainView;

import java.util.*;

import javax.swing.table.DefaultTableModel;

public class MainViewTableModel extends DefaultTableModel {

    static Vector<String> columns = new Vector<>();

    static {
        columns.addElement("���");
        columns.addElement("����");
        columns.addElement("�绰");
        columns.addElement("����");
        columns.addElement("��ַ");
    }

    public MainViewTableModel() {
        super(null, columns);
    }

    private static MainViewTableModel mainViewTableModel = new MainViewTableModel();

    public static MainViewTableModel assembleModel(Vector<Vector<Object>> data) {
        mainViewTableModel.setDataVector(data, columns);
        return mainViewTableModel;
    }

    public static void updateModel(Vector<Vector<Object>> data) {
        mainViewTableModel.setDataVector(data, columns);

    }

    public static Vector<String> getColumns() {
        return columns;
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
