package handler;

import view.addView.AddView;
import view.editView.EditView;
import view.mainView.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainViewHandler implements ActionListener {
    private MainView mainView;

    public MainViewHandler(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jBtn = (JButton) e.getSource();
        String text = jBtn.getText();
        if ("����".equals(text)) {
            new AddView(mainView);
        } else if ("�޸�".equals(text)) {
            String[] phones = mainView.getSelectedPhones();
            if (phones.length == 0) {
                JOptionPane.showMessageDialog(mainView, "��ѡ��Ҫ�޸ĵ���");
                return;
            }
            if (phones.length != 1) {
                JOptionPane.showMessageDialog(mainView, "һ��ֻ���޸�һ��");
                return;
            }
            new EditView(mainView, phones[0]);
        } else if ("ɾ��".equals(text)) {
            String[] phones = mainView.getSelectedPhones();
            if (phones.length == 0) {
                JOptionPane.showMessageDialog(mainView, "��ѡ��Ҫɾ������");
                return;
            }
            int option = JOptionPane.showConfirmDialog(mainView, "��ȷ��Ҫɾ��ѡ���" + phones.length + "����", "ȷ��ɾ��", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
            boolean deleteResult = mainView.getContactBook().deleteContact(phones);
            if (deleteResult) {
                mainView.reloadTable();
            } else {
                JOptionPane.showMessageDialog(mainView, "ɾ��ʧ��");
            }

        } else if ("����".equals(text)) {
            mainView.setPageNum(1);
            mainView.reloadTable();

        } else if ("��һҳ".equals(text)) {
            mainView.setPageNum(mainView.getPageNum() - 1);
            mainView.reloadTable();
        } else if ("��һҳ".equals(text)) {
            mainView.setPageNum(mainView.getPageNum() + 1);
            mainView.reloadTable();
        }

    }

}
