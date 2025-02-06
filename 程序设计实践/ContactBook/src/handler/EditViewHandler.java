package handler;

import contactBook.Contact;
import view.editView.EditView;
import view.mainView.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class EditViewHandler implements ActionListener {
    private EditView editView;
    private MainView mainView;

    public EditViewHandler(EditView editView, MainView mainView) {
        this.editView = editView;
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jBtn = (JButton) e.getSource();
        String text = jBtn.getText();
        if ("�޸���Ϣ".equals(text)) {
            Contact newContact = editView.buildContact();
            if (newContact.getName().equals("null")) {
                JOptionPane.showMessageDialog(editView, "��ϵ����������Ϊ�գ�");
                return;
            }
            if (!Pattern.matches("^1[3-9]\\d{9}$", newContact.getPhone())) {
                JOptionPane.showMessageDialog(editView, "����д��ȷ����ϵ�绰��");
                return;
            }
            boolean editResult = mainView.getContactBook().editContact(editView.getOldPhone(), newContact);
            if (editResult) {
                mainView.reloadTable();
                editView.dispose();
            } else {
                JOptionPane.showMessageDialog(editView, "�޸�ʧ�ܣ��ֻ����Ѵ��ڣ�");
            }
        }
    }
}
