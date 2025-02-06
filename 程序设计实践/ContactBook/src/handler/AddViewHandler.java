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
        if ("�����ϵ��".equals(text)) {
            Contact newContact = addView.buildContact();
            if ("null".equals(newContact.getName())) {
                JOptionPane.showMessageDialog(addView, "��ϵ����������Ϊ�գ�");
                return;
            }
            if (!Pattern.matches("^1[3-9]\\d{9}$", newContact.getPhone())) {
                JOptionPane.showMessageDialog(addView, "����д��ȷ����ϵ�绰��");
                return;
            }
            boolean addResult = mainView.getContactBook().addContact(addView.buildContact());
            if (addResult) {
                mainView.reloadTable();
                addView.dispose();
            } else {
                JOptionPane.showMessageDialog(addView, "���ʧ��,�ֻ����Ѵ��ڣ�");
            }
        }
    }


}
