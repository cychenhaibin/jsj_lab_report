package view.mainView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MainViewCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (row % 2 == 0) {
            setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(Color.WHITE);
        }
        setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
