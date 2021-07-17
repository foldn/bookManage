package gui;

import toolclass.Books;
import toolclass.DruidJDBCUtils;
import toolclass.Users;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * @author FOLDN
 */
public class ManageExhibition {
    public ManageExhibition(){}
    public ManageExhibition(String user_account){
        this.user_account = user_account;
    }
    String user_account;
    String user_name;
    String user_type;

    // 创建一个主窗口
    JFrame frame = new JFrame("游客界面");
    // 创建一个标签页面板，用于存放标签页
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);
    // 创建一个层次结构面板,来存放这些面板(注意：添加进结构面板要在添加功能标签之前，否则这些标签不会显示出来)
    JLayeredPane layeredPane = new JLayeredPane();
    // 创建标签页
    JPanel addBook = new JPanel();
    JPanel deleteBook = new JPanel();
    JPanel userExhibition = new JPanel();
    JPanel self = new JPanel();

    public JFrame openManageExhibition(){
        layeredPane.add(addBook);
        layeredPane.add(deleteBook);
        layeredPane.add(userExhibition);
        layeredPane.add(self);
        layeredPane.moveToBack(self);
        // 添加标签页
        tabbedPane.addTab("添加书籍",new MainExhibition().change(
                new ImageIcon("src\\images\\addBook.png"),0.1),addBook,"添加书籍");
        tabbedPane.addTab("删除书籍",new MainExhibition().change(
                new ImageIcon("src\\images\\deleteBook.png"),0.1),deleteBook,"删除书籍");
        tabbedPane.addTab("账户一览",new MainExhibition().change(
                new ImageIcon("src\\images\\userExhibition.png"),0.1),userExhibition,"展示所有账户");
        tabbedPane.addTab("个人信息",new MainExhibition().change(new ImageIcon("src\\images\\self.png"),0.1)
                ,self,"查看个人信息");
        queryPersonal();
        tabbedPane.setSelectedComponent(self);
        // 为标签页添加事件
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex){
                    case 0:{
                        addBook();
                        break;
                    }
                    case 1:{
                        deleteBook();
                        break;
                    }
                    case 2:{
                        userExhibition();
                        break;
                    }
                    case 3:{
                        queryPersonal();
                        break;
                    }
                    default: break;
                }
            }
        });
        // 将标签页面加入frame中
        frame.add(tabbedPane, BorderLayout.WEST);
        frame.add(layeredPane);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return this.frame;
    }
    // 添加书籍功能实现
    public void addBook(){
        // 首先清除所有组件
        addBook.removeAll();
        // 创建一个表格再该组件中显示所有的书籍
        Vector title = new Vector();
        title.addElement("书的编号");
        title.addElement("书名");
        title.addElement("作者");
        title.addElement("类型");
        title.addElement("价格");
        title.addElement("是否被借走");
        title.addElement("入库时间");
        Vector data = new Books().getData();
        // 我们需要创建一个表格来存放从数据库中得到的书籍数据
        JTable table = new JTable(data,title);
        //设置表格大小
        table.setPreferredScrollableViewportSize(new Dimension(1020,200));
        table.setEnabled(false);
        // 创建一个多行文本域，显示按钮操作详情
        JTextArea ta = new JTextArea(20,60);
        // 创建一个面板存放输入书名的框和按钮
        JPanel addBookPanel = new JPanel();
        // 创建一个文本输入框和标签
        JLabel addTip = new JLabel("请输入您要添加的书：（格式为：名称-作者-类型-价格）");
        JTextField bookName = new JTextField(20);
        // 创建一个JPanel面板来存放两个按钮
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加");
        // 为添加按钮增加事件监听
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String book = bookName.getText();
                boolean result = new Books().addBook(book);
                SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                if (result){
                    ta.append("添加成功     --"+date.format(new Date()));
                    JOptionPane.showMessageDialog(addBook,"添加成功","添加成功",JOptionPane.INFORMATION_MESSAGE);
                    addBook();
                }else {
                    ta.append("添加失败     --"+date.format(new Date()));
                    JOptionPane.showMessageDialog(addBook,"出了一些小问题,请检查你的书籍格式是否正确","警告",JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        JButton refresh = new JButton("刷新");
        // 为刷新按钮增加事件监听
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                ta.append("刷新成功     --"+date.format(new Date()));
                JOptionPane.showMessageDialog(addBook,"刷新成功","提示",JOptionPane.INFORMATION_MESSAGE);
                addBook();
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(refresh);
        addBookPanel.add(addTip);
        addBookPanel.add(bookName);
        addBookPanel.add(buttonPanel);

        Box mainBox = Box.createVerticalBox();
        mainBox.add(new JScrollPane(table));
        mainBox.add(addBookPanel);
        mainBox.add(ta);
        addBook.add(mainBox);
        layeredPane.moveToBack(addBook);
    }
    // 删除书籍功能实现
    public void deleteBook(){
        // 首先清除所有组件
        deleteBook.removeAll();
        // 创建一个表格再该组件中显示所有的书籍
        Vector title = new Vector();
        title.addElement("书的编号");
        title.addElement("书名");
        title.addElement("作者");
        title.addElement("类型");
        title.addElement("价格");
        title.addElement("是否被借走");
        title.addElement("入库时间");
        Vector data = new Books().getData();
        // 我们需要创建一个表格来存放从数据库中得到的书籍数据
        JTable table = new JTable(data,title);
        //设置表格大小
        table.setPreferredScrollableViewportSize(new Dimension(1020,200));
        table.setEnabled(false);
        // 创建一个多行文本域，显示按钮操作详情
        JTextArea ta = new JTextArea(20,60);
        // 创建一个面板存放输入书名的框和按钮
        JPanel addBookPanel = new JPanel();
        // 创建一个文本输入框和标签
        JLabel addTip = new JLabel("请输入您要删除的书的名称：");
        JTextField bookName = new JTextField(20);
        // 创建一个JPanel面板来存放两个按钮
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("删除");
        // 为添加按钮增加事件监听
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String book = bookName.getText();
                boolean result = new Books().deleteBook(book);
                SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                if (result){
                    ta.append("删除成功     --"+date.format(new Date()));
                    JOptionPane.showMessageDialog(addBook,"删除成功","删除成功",JOptionPane.INFORMATION_MESSAGE);
                    deleteBook();
                }else {
                    ta.append("删除失败     --"+date.format(new Date()));
                    JOptionPane.showMessageDialog(addBook,"出了一些小问题,请检查你的书籍是否存在库中","警告",JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        JButton refresh = new JButton("刷新");
        // 为刷新按钮增加事件监听
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                ta.append("刷新成功     --"+date.format(new Date()));
                JOptionPane.showMessageDialog(addBook,"刷新成功","提示",JOptionPane.INFORMATION_MESSAGE);
                deleteBook();
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(refresh);
        addBookPanel.add(addTip);
        addBookPanel.add(bookName);
        addBookPanel.add(buttonPanel);

        Box mainBox = Box.createVerticalBox();
        mainBox.add(new JScrollPane(table));
        mainBox.add(addBookPanel);
        mainBox.add(ta);
        deleteBook.add(mainBox);
        layeredPane.moveToBack(deleteBook);


    }
    // 账户一览功能实现
    public void userExhibition(){
        // 首先清除所有的组件
        userExhibition.removeAll();
        Vector title = new Vector();
        title.addElement("用户编号");
        title.addElement("用户名");
        title.addElement("用户账号");
        title.addElement("用户密码");
        title.addElement("用户类型");
        title.addElement("账号创建时间");
        Vector data = new Users().getData();
        // 我们需要创建一个表格来存放从数据库中得到的书籍数据
        JTable table = new JTable(data,title);
        table.setEnabled(false);
        //设置表格大小
        table.setPreferredScrollableViewportSize(new Dimension(1020,670));
        table.setEnabled(false);
        userExhibition.add(new JScrollPane(table));
        layeredPane.moveToBack(userExhibition);
    }
    // 查看个人信息的功能
    public void queryPersonal(){
        // 为防止组件重复创建，先清除组件
        self.removeAll();
        // 创建一个标签，存放游客头像
        JLabel icon = new JLabel(new MainExhibition().change(
                new ImageIcon("src\\images\\如果再谈恋爱 我想从收到一束花开始_109951165852619859.jpg"),0.3));
        // 创建一个JPanel面板，存放我们所需要的提示标签
        JPanel topPanel = new JPanel();
        //创建提示标签和对应的字符串
        String date  = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
        getUserData();
        JLabel name = new JLabel("用户名为："+user_name);
        JLabel account = new JLabel("用户账号为："+this.user_account);
        JLabel identity = new JLabel("用户身份为："+user_type);
        JLabel time = new JLabel("登陆时间为："+date);
        //创建一个垂直的盒子存放这些标签
        Box labelBox = Box.createVerticalBox();
        labelBox.add(icon);
        labelBox.add(name);
        labelBox.add(account);
        labelBox.add(identity);
        labelBox.add(time);
        topPanel.add(labelBox,BorderLayout.CENTER);
        self.add(topPanel,BorderLayout.CENTER);
        self.add(labelBox,BorderLayout.SOUTH);
        self.setPreferredSize(new Dimension(1020,670));
        layeredPane.moveToBack(self);
    }
    // 创建一个方法根据user_account来从数据库中获取数据
    public void getUserData(){
        String user_account = this.user_account;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql = "select * from users where user_account = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user_account);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user_name = resultSet.getString("user_name");
                user_type = resultSet.getString("user_type");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(resultSet,preparedStatement,connection);

        }
    }



}
