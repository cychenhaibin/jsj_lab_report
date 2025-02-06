package view.addView;

import contactBook.Contact;
import handler.AddViewHandler;
import view.mainView.MainView;

import javax.swing.*;
import java.awt.*;

public class AddView extends JDialog {
    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
    JLabel nameLabel = new JLabel("姓名", JLabel.RIGHT);
    JTextField nameText = new JTextField();
    JLabel phoneLabel = new JLabel("电话", JLabel.RIGHT);
    JTextField phoneText = new JTextField();
    JLabel emailLabel = new JLabel("邮箱", JLabel.RIGHT);
    JTextField emailText = new JTextField();
    JLabel addressLabel = new JLabel("住址", JLabel.RIGHT);
    JTextField addressText = new JTextField();
    JButton addBtn = new JButton("添加联系人");

    public AddView(MainView mainView) {
        super(mainView, "添加联系人", true);
        AddViewHandler addViewHandler = new AddViewHandler(this, mainView);

        nameLabel.setPreferredSize(new Dimension(30, 30));
        nameText.setPreferredSize(new Dimension(200, 30));
        jPanel.add(nameLabel);
        jPanel.add(nameText);

        phoneLabel.setPreferredSize(new Dimension(30, 30));
        phoneText.setPreferredSize(new Dimension(200, 30));
        jPanel.add(phoneLabel);
        jPanel.add(phoneText);

        emailLabel.setPreferredSize(new Dimension(30, 30));
        emailText.setPreferredSize(new Dimension(200, 30));
        jPanel.add(emailLabel);
        jPanel.add(emailText);

        addressLabel.setPreferredSize(new Dimension(30, 30));
        addressText.setPreferredSize(new Dimension(200, 30));
        jPanel.add(addressLabel);
        jPanel.add(addressText);

        addBtn.addActionListener(addViewHandler);
        jPanel.add(addBtn);


        getContentPane().add(jPanel);
        setSize(300, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public Contact buildContact() {
        Contact contact = new Contact();
        if ("".equals(nameText.getText())) {
            contact.setName("null");
        } else {
            contact.setName(nameText.getText());
        }

        if ("".equals(phoneText.getText())) {
            contact.setPhone("null");
        } else {
            contact.setPhone(phoneText.getText());
        }

        if ("".equals(emailText.getText())) {
            contact.setEmail("null");
        } else {
            contact.setEmail(emailText.getText());
        }

        if ("".equals(addressText.getText())) {
            contact.setAddress("null");
        } else {
            contact.setAddress(addressText.getText());
        }
        return contact;
    }
}
