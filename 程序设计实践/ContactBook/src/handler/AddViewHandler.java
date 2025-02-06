package handler;

import contactBook.Contact;
import view.addView.AddView;
import view.mainView.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class AddViewHandler implements ActionListener {
    private AddView addView;
    private MainView mainView;

    public AddViewHandler(AddView addView, MainView mainView) {
        this.addView = addView;
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jBtn = (JButton) e.getSource();
        String text = jBtn.getText();
        if ("添加联系人".equals(text)) {
            Contact newContact = addView.buildContact();
            if ("null".equals(newContact.getName())) {
                JOptionPane.showMessageDialog(addView, "联系人姓名不能为空！");
                return;
            }
            if (!Pattern.matches("^1[3-9]\\d{9}$", newContact.getPhone())) {
                JOptionPane.showMessageDialog(addView, "请填写正确的联系电话！");
                return;
            }
            boolean addResult = mainView.getContactBook().addContact(addView.buildContact());
            if (addResult) {
                mainView.reloadTable();
                addView.dispose();
            } else {
                JOptionPane.showMessageDialog(addView, "添加失败,手机号已存在！");
            }
        }
    }


}
