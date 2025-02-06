package handler;

import contactBook.Contact;
import view.mainView.MainViewTable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainViewTableHandler extends MouseAdapter implements ActionListener {
    MainViewTable mainViewTable;
    JPopupMenu popupMenu;
    private Contact contact;

    public MainViewTableHandler(MainViewTable mainViewTable, JPopupMenu popupMenu) {
        this.mainViewTable = mainViewTable;
        this.popupMenu = popupMenu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        String text = item.getText();
        if ("复制姓名".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getName());
            clip.setContents(tText, null);

        } else if ("复制电话".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getPhone());
            clip.setContents(tText, null);

        } else if ("复制邮箱".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getEmail());
            clip.setContents(tText, null);

        } else if ("复制地址".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getAddress());
            clip.setContents(tText, null);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            //通过点击位置找到点击为表格中的行
            int focusedRowIndex = mainViewTable.rowAtPoint(e.getPoint());
            int focusedColumnIndex = mainViewTable.columnAtPoint(e.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }
            if (focusedColumnIndex == 0) {
                return;
            }
            //将表格所选项设为当前右键点击的行
            mainViewTable.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            String name = mainViewTable.getValueAt(focusedRowIndex, 1).toString();
            String phone = mainViewTable.getValueAt(focusedRowIndex, 2).toString();
            String email = mainViewTable.getValueAt(focusedRowIndex, 3).toString();
            String address = mainViewTable.getValueAt(focusedRowIndex, 4).toString();
            contact = new Contact(name, phone, email, address);
            //弹出菜单
            popupMenu.show(mainViewTable, e.getX(), e.getY());


        }
    }
}
