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
        if ("��������".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getName());
            clip.setContents(tText, null);

        } else if ("���Ƶ绰".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getPhone());
            clip.setContents(tText, null);

        } else if ("��������".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getEmail());
            clip.setContents(tText, null);

        } else if ("���Ƶ�ַ".equals(text)) {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(contact.getAddress());
            clip.setContents(tText, null);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            //ͨ�����λ���ҵ����Ϊ����е���
            int focusedRowIndex = mainViewTable.rowAtPoint(e.getPoint());
            int focusedColumnIndex = mainViewTable.columnAtPoint(e.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }
            if (focusedColumnIndex == 0) {
                return;
            }
            //�������ѡ����Ϊ��ǰ�Ҽ��������
            mainViewTable.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            String name = mainViewTable.getValueAt(focusedRowIndex, 1).toString();
            String phone = mainViewTable.getValueAt(focusedRowIndex, 2).toString();
            String email = mainViewTable.getValueAt(focusedRowIndex, 3).toString();
            String address = mainViewTable.getValueAt(focusedRowIndex, 4).toString();
            contact = new Contact(name, phone, email, address);
            //�����˵�
            popupMenu.show(mainViewTable, e.getX(), e.getY());


        }
    }
}
