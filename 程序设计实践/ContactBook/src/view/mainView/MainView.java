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
    JButton addBtn = new JButton("增加");
    JButton editBtn = new JButton("修改");
    JButton deleteBtn = new JButton("删除");
    JTextField searchText = new JTextField(15);
    JButton searchBtn = new JButton("搜索");

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton preBtn = new JButton("上一页");
    JButton nextBtn = new JButton("下一页");
    MainViewTable mainViewTable = new MainViewTable();

    private int pageNum = 1;
    private int pageSize = 20;
    MainViewHandler mainViewHandler;


    private ContactBook contactBook;

    public MainView() {
        super("通信管理系统");
        contactBook = new ContactBook();
        //初始化界面
        this.initContactBookJFrame();
        //初始化菜单
        this.initJPanel();
    }

    private void initContactBookJFrame() {
        //设置界面宽高
        this.setSize(800, 800);
        //设置界面标题
        this.setTitle("通信管理系统");
        //设置界面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(3);
        //取消默认的居中放置，只有取消了才会按照xy轴的方式添加组件
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


        //顶部组件
        topPanel.add(addBtn);
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);
        topPanel.add(searchText);
        topPanel.add(searchBtn);
        contentPane.add(topPanel, BorderLayout.NORTH);

        //中间组件
        TableData data = contactBook.getContacts("", pageNum, pageSize);
        MainViewTableModel mainViewTableModel = MainViewTableModel.assembleModel(data.getDataList());
        showPreNext(data.getTotal());
        mainViewTable.setModel(mainViewTableModel);
        mainViewTable.renderRule();
        JScrollPane jScrollPane = new JScrollPane(mainViewTable);
        contentPane.add(jScrollPane, BorderLayout.CENTER);

        //底部组件
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
        //回车搜索
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
