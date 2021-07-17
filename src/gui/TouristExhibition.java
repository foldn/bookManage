package gui;

import toolclass.Books;
import toolclass.DruidJDBCUtils;

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
import java.util.*;


/**
 * @author FOLDN
 */
public class TouristExhibition {
    public TouristExhibition(){}

    public TouristExhibition(String user_account){
        this.user_account = user_account;
    }
    String user_account;
    String user_name;
    String user_type;
    // 创建一个主窗口
    JFrame frame = new JFrame("游客界面");
    // 创建一个标签页面板，用于存放标签页
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);
    // 创建相关功能面版
    JPanel self = new JPanel();
    JPanel bookExhibition = new JPanel();
    JPanel borrowBook = new JPanel();
    JPanel borrowBookAgain =  new JPanel();
    JPanel returnBook = new JPanel();
    // 创建一个层次结构面板,来存放这些面板(注意：添加进结构面板要在添加功能标签之前，否则这些标签不会显示出来)
    JLayeredPane layeredPane = new JLayeredPane();
    // 创建一个集合来存放用户借的书和时间
    HashMap<String,SimpleDateFormat> bookDesk = new HashMap<>();
    // 首先需要一个方法来打开游客界面
    public JFrame openTouristPanel(){
        layeredPane.add(bookExhibition);
        layeredPane.add(borrowBook);
        layeredPane.add(returnBook);
        layeredPane.add(borrowBookAgain);
        layeredPane.add(self);
        // 将查看书籍页面放在最上层
        layeredPane.moveToBack(bookExhibition);
        // 添加相关功能标签
        tabbedPane.addTab("查看书籍",new MainExhibition().change(new ImageIcon("src\\images\\bookExhibition.png"),0.1)
                ,bookExhibition,"查看书籍");
        tabbedPane.addTab("借阅书籍",new MainExhibition().change(new ImageIcon("src\\images\\borrowBook.png"),0.1)
                ,borrowBook,"借书");
        tabbedPane.addTab("归还书籍",new MainExhibition().change(new ImageIcon("src\\images\\returnBook.png"),0.1)
                ,returnBook,"还书");
        tabbedPane.addTab("个人信息",new MainExhibition().change(new ImageIcon("src\\images\\self.png"),0.1)
                ,self,"查看个人信息");
        getBookExhibition();
        tabbedPane.setSelectedComponent(bookExhibition);
        // 为标签页面板添加事件监听
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex){
                    case 0:{
                        getBookExhibition();
                        break;
                    }
                    case 1:{
                        borrowBook();
                        break;
                    }
                    case 2:{
                        returnBook();
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
        return frame;
    }
    // 查看有哪些书的功能，使用一个返回JPanel面板的方法来实现
    public void getBookExhibition(){
        // 首先，将原有的组件全部清除，否则会导致每一次都创建新的组件
        bookExhibition.removeAll();
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
        table.setEnabled(false);
        //设置表格大小
        table.setPreferredScrollableViewportSize(new Dimension(1020,670));
        bookExhibition.add(new JScrollPane(table));
        layeredPane.moveToBack(bookExhibition);
    }
    // 查看个人信息的功能
    public void queryPersonal(){
        // 为防止组件重复创建，先清除组件
        self.removeAll();
        // 创建一个标签，存放游客头像
        JLabel icon = new JLabel(new MainExhibition().change(
                new ImageIcon("src\\images\\写作业必备歌单 纯音乐_109951164891790389.jpg"),0.3));
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
        topPanel.add(labelBox);
        //创建一个多行文本域来存放书名字
        Box bookDeskBox = Box.createVerticalBox();
        JLabel bookDesk = new JLabel("借的书：");
        JTextArea ta = new JTextArea(20,10);
        // 为ta添加数据
        Set<Map.Entry<String, SimpleDateFormat>> books = this.bookDesk.entrySet();
        for (Map.Entry entry: books){
            ta.append("您于"+((SimpleDateFormat)entry.getValue()).format(new Date())+"借阅了"+"《"+entry.getKey()+"》");
        }
        bookDeskBox.add(bookDesk);
        bookDeskBox.add(ta);
        Box mainBox = Box.createVerticalBox();
        mainBox.add(topPanel);
        mainBox.add(labelBox);
        mainBox.add(bookDeskBox);
        self.add(mainBox);
        layeredPane.moveToBack(self);

    }
    // 游客有借书功能
    public void borrowBook(){
        // 首先清除组件
        borrowBook.removeAll();
        JLabel borrowBookTips = new JLabel("请输入您要借的书的名字");
        JTextField inputBookName = new JTextField();
        Box topBox = Box.createVerticalBox();
        Box mainBox = Box.createVerticalBox();
        JButton button = new JButton("借书");
        JTextArea ta = new JTextArea(20,60);
        topBox.add(borrowBookTips);
        topBox.add(inputBookName);
        // 创建一个Panel来存放按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);
        topBox.add(buttonPanel);
        mainBox.add(topBox);
        //创建一个面板来存放ta
        JPanel taPanel = new JPanel();
        taPanel.add(ta);
        mainBox.add(taPanel);
        // 为查询按钮添加监听器
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputBookName.getText();
                boolean result = new Books().borrowBook(text);
                if (result){
                    SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    String tips = "您于"+date.format(new Date())+"借阅了"+ "《"+text+"》"+ "请在15天内归还";
                    // 每次成功借书之后添加书名和时间进入书桌集合
                    bookDesk.put(text,date);
                    ta.append(tips);
                    JOptionPane.showMessageDialog(borrowBook,"借书成功","借书成功",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(borrowBook,"当前没有您想要的这本书","",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        borrowBook.add(mainBox);
        layeredPane.moveToBack(borrowBook);
    }
    // 游客有还书功能
    public void returnBook(){
        // 首先清除组件
        returnBook.removeAll();
        JLabel borrowBookTips = new JLabel("请输入您要还的书的名字");
        JTextField inputBookName = new JTextField();
        Box topBox = Box.createVerticalBox();
        Box mainBox = Box.createVerticalBox();
        JButton button = new JButton("还书");
        JTextArea ta = new JTextArea(20,60);
        topBox.add(borrowBookTips);
        topBox.add(inputBookName);
        // 创建一个Panel来存放按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);
        topBox.add(buttonPanel);
        mainBox.add(topBox);
        //创建一个面板来存放ta
        JPanel taPanel = new JPanel();
        taPanel.add(ta);
        mainBox.add(taPanel);
        // 为查询按钮添加监听器
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputBookName.getText();
                boolean result = new Books().returnBook(text);
                if (result){
                    SimpleDateFormat date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    String tips = "您于"+date.format(new Date())+"归还了"+ "《"+text+"》" ;
                    // 每次还书之后从书桌集合中删除相应的键值对
                    bookDesk.remove(text,date);
                    ta.append(tips);
                    JOptionPane.showMessageDialog(borrowBook,"还书成功","还书成功",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(borrowBook,"似乎出了点问题，请您重试一边","",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        returnBook.add(mainBox);
        layeredPane.moveToBack(returnBook);
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
