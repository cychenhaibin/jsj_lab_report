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
        if ("修改信息".equals(text)) {
            Contact newContact = editView.buildContact();
            if (newContact.getName().equals("null")) {
                JOptionPane.showMessageDialog(editView, "联系人姓名不能为空！");
                return;
            }
            if (!Pattern.matches("^1[3-9]\\d{9}$", newContact.getPhone())) {
                JOptionPane.showMessageDialog(editView, "请填写正确的联系电话！");
                return;
            }
            boolean editResult = mainView.getContactBook().editContact(editView.getOldPhone(), newContact);
            if (editResult) {
                mainView.reloadTable();
                editView.dispose();
            } else {
                JOptionPane.showMessageDialog(editView, "修改失败，手机号已存在！");
            }
        }
    }
}
