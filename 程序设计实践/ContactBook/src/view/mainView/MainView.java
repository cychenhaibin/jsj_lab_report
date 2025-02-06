package view.mainView;

import contactBook.ContactBook;
import contactBook.TableData;
import handler.MainViewHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainView extends JFrame implements KeyListener {

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton addBtn = new JButton("����");
    JButton editBtn = new JButton("�޸�");
    JButton deleteBtn = new JButton("ɾ��");
    JTextField searchText = new JTextField(15);
    JButton searchBtn = new JButton("����");

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton preBtn = new JButton("��һҳ");
    JButton nextBtn = new JButton("��һҳ");
    MainViewTable mainViewTable = new MainViewTable();

    private int pageNum = 1;
    private int pageSize = 20;
    MainViewHandler mainViewHandler;


    private ContactBook contactBook;

    public MainView() {
        super("ͨ�Ź���ϵͳ");
        contactBook = new ContactBook();
        //��ʼ������
        this.initContactBookJFrame();
        //��ʼ���˵�
        this.initJPanel();
    }

    private void initContactBookJFrame() {
        //���ý�����
        this.setSize(800, 800);
        //���ý������
        this.setTitle("ͨ�Ź���ϵͳ");
        //���ý����ö�
        this.setAlwaysOnTop(true);
        //���ý������
        this.setLocationRelativeTo(null);
        //���ùر�ģʽ
        this.setDefaultCloseOperation(3);
        //ȡ��Ĭ�ϵľ��з��ã�ֻ��ȡ���˲Żᰴ��xy��ķ�ʽ������
//        this.setLayout(null);
        this.setVisible(true);
    }

    private void showPreNext(int total) {
        if (pageNum == 1) {
            preBtn.setEnabled(false);

        } else {
            preBtn.setEnabled(true);
        }
        int pageCount;
        if (total % pageSize == 0) {
            pageCount = total / pageSize;
        } else {
            pageCount = total / pageSize + 1;
        }
        if (pageNum == pageCount) {
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }
    }

    private void initJPanel() {
        mainViewHandler = new MainViewHandler(this);
        Container contentPane = this.getContentPane();

        addBtn.addActionListener(mainViewHandler);
        editBtn.addActionListener(mainViewHandler);
        deleteBtn.addActionListener(mainViewHandler);
        searchBtn.addActionListener(mainViewHandler);
        searchText.addKeyListener(this);


        //�������
        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);
        topPanel.add(searchText);
        topPanel.add(searchBtn);
        contentPane.add(topPanel, BorderLayout.NORTH);

        //�м����
        TableData data = contactBook.getContacts("", pageNum, pageSize);
        MainViewTableModel mainViewTableModel = MainViewTableModel.assembleModel(data.getDataList());
        showPreNext(data.getTotal());
        mainViewTable.setModel(mainViewTableModel);
        mainViewTable.renderRule();
        JScrollPane jScrollPane = new JScrollPane(mainViewTable);
        contentPane.add(jScrollPane, BorderLayout.CENTER);

        //�ײ����
        preBtn.addActionListener(mainViewHandler);
        nextBtn.addActionListener(mainViewHandler);
        bottomPanel.add(preBtn);
        bottomPanel.add(nextBtn);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);


    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ContactBook getContactBook() {
        return contactBook;
    }

    public void setContactBook(ContactBook contactBook) {
        this.contactBook = contactBook;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //�س�����
        if(e.getKeyCode()==10){
            reloadTable();
        }
    }

    public void reloadTable() {
        TableData data = contactBook.getContacts(searchText.getText().trim(), pageNum, pageSize);
        MainViewTableModel.updateModel(data.getDataList());
        showPreNext(data.getTotal());
        mainViewTable.renderRule();
    }

    public String[] getSelectedPhones() {
        int[] selectedRows = mainViewTable.getSelectedRows();
        String[] phones = new String[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            int rowIndex = selectedRows[i];
            Object phone = mainViewTable.getValueAt(rowIndex, 2);
            phones[i] = phone.toString();
        }
        return phones;
    }
}
