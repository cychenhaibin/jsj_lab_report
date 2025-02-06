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
        if ("增加".equals(text)) {
            new AddView(mainView);
        } else if ("修改".equals(text)) {
            String[] phones = mainView.getSelectedPhones();
            if (phones.length == 0) {
                JOptionPane.showMessageDialog(mainView, "请选择要修改的行");
                return;
            }
            if (phones.length != 1) {
                JOptionPane.showMessageDialog(mainView, "一次只能修改一行");
                return;
            }
            new EditView(mainView, phones[0]);
        } else if ("删除".equals(text)) {
            String[] phones = mainView.getSelectedPhones();
            if (phones.length == 0) {
                JOptionPane.showMessageDialog(mainView, "请选择要删除的行");
                return;
            }
            int option = JOptionPane.showConfirmDialog(mainView, "你确认要删除选择的" + phones.length + "行吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
            boolean deleteResult = mainView.getContactBook().deleteContact(phones);
            if (deleteResult) {
                mainView.reloadTable();
            } else {
                JOptionPane.showMessageDialog(mainView, "删除失败");
            }

        } else if ("搜索".equals(text)) {
            mainView.setPageNum(1);
            mainView.reloadTable();

        } else if ("上一页".equals(text)) {
            mainView.setPageNum(mainView.getPageNum() - 1);
            mainView.reloadTable();
        } else if ("下一页".equals(text)) {
            mainView.setPageNum(mainView.getPageNum() + 1);
            mainView.reloadTable();
        }

    }

}
