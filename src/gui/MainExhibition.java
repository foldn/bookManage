package gui;

import toolclass.Users;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author FOLDN
 */
public class MainExhibition {
    // 首先创建一个主窗口
    JFrame frame = new JFrame("图书信息管理系统");
    // 创建一个panel面板放在右侧作为主页面，其中放置一张图片用以美观
    JLabel label = new JLabel(new ImageIcon("src\\images\\library.jpg"));
    // 创建一个标签页面板，用于存放标签页
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);
    // 创建三个窗口作为三个标签页对应的组件
    JPanel managePanel = new JPanel();
    JPanel touristPanel = new JPanel();
    JPanel registered = new JPanel();
    JPanel mainPanel = new JPanel();
    // 创建一个层次结构面板
    JLayeredPane layeredPane = new JLayeredPane();
    // 创建ImageIcon对象，存放图片
    ImageIcon manageIcon = new ImageIcon("src\\images\\manage.png");
    ImageIcon touristIcon = new ImageIcon("src\\images\\tourist.png");
    ImageIcon loginIcon = new ImageIcon("src\\images\\login.png");
    ImageIcon mainIon = new ImageIcon("src\\images\\main.png");
    public void init(){
        // 将标签页对应的组件添加进层次结构面板中
        layeredPane.add(managePanel);
        layeredPane.add(touristPanel);
        layeredPane.add(registered);
        mainPanel.add(label);
        layeredPane.add(mainPanel);
        layeredPane.moveToBack(mainPanel);
        // 为标签页面板添加标签页
        tabbedPane.addTab("管理",change(manageIcon,0.1),managePanel,"管理员登录");
        tabbedPane.addTab("游客",change(touristIcon,0.1),touristPanel,"游客登录");
        tabbedPane.addTab("注册",change(loginIcon,0.1), registered,"注册");
        tabbedPane.addTab("主页",change(mainIon,0.1),mainPanel,"主页");
        // 设置标签面板默认选项
        tabbedPane.setSelectedComponent(mainPanel);
        // 为标签页添加监听事件
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex){
                    case 0:{
                        manageInit();
                    break;
                    }
                    case 1:{
                        touristInit();
                    break;
                    }
                    case 2:{
                        registered();
                    break;
                    }
                    case 3:{
                        layeredPane.moveToBack(mainPanel);
                    break;
                    }
                    default:break;
                }
            }
        });
        // 将标签面版加入
        frame.add(tabbedPane,BorderLayout.WEST);
        frame.add(layeredPane);
        // 默认显示容器中最后一张

        // 设置最佳大小、可见性以及关闭事件
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    // 创建一个改变图标大小的方法
    public ImageIcon change(ImageIcon image,double i){//  i 为放缩的倍数

            int width=(int) (image.getIconWidth()*i);
            int height=(int) (image.getIconHeight()*i);
            Image img=image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);//第三个值可以去查api是图片转化的方式
            ImageIcon image2=new ImageIcon(img);
            return image2;
    }
    //三个页面初始化方法
    public void manageInit(){
        // 先清除组件上原有的东西，防止我们每一次触发事件都产生新的组件
        managePanel.removeAll();
        // 创建一个按钮组件
        JButton loginButton = new JButton("登录");
        Box box = Box.createVerticalBox();
        JLabel accountLabel = new JLabel("请输入你的账号：");
        JTextField account = new JTextField("例如：123");
        JLabel passwordLabel = new JLabel("请输入你的密码：");
        JTextField password = new JTextField("例如：123");
        box.add(accountLabel);
        box.add(account);
        box.add(passwordLabel);
        box.add(password);
        JPanel buttonPane = new JPanel();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Users user = getRegisteredUser(account, password);
                boolean loginResult = user.login(user.getUser_account(), user.getUser_password());
                if (loginResult && new Users().isManage(user.getUser_account())){
                    JOptionPane.showMessageDialog(touristPanel,"登陆成功，即将打开管理员页面"
                            ,"登陆成功",JOptionPane.INFORMATION_MESSAGE);
                    JFrame tourist = new ManageExhibition(user.getUser_account()).openManageExhibition();
                    tourist.setBounds(frame.getX(),frame.getY(),frame.getWidth(),frame.getHeight());
                }else{
                    JOptionPane.showMessageDialog(touristPanel,"似乎出了一点小问题，请检查你的账号和密码是否错误"
                            ,"登陆成功",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPane.add(loginButton);
        box.add(buttonPane);
        managePanel.add(box);
        layeredPane.moveToBack(managePanel);
    }
    public void touristInit(){
        // 先清除组件上原有的东西，防止我们每一次触发事件都产生新的组件
        touristPanel.removeAll();
        // 创建一个按钮组件
        JButton loginButton = new JButton("登录");
        Box box = Box.createVerticalBox();
        JLabel accountLabel = new JLabel("请输入你的账号：");
        JTextField account = new JTextField("例如：123");
        JLabel passwordLabel = new JLabel("请输入你的密码：");
        JTextField password = new JTextField("例如：123");
        box.add(accountLabel);
        box.add(account);
        box.add(passwordLabel);
        box.add(password);
        JPanel buttonPane = new JPanel();
        // 为登录按钮添加事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Users user = getRegisteredUser(account, password);
                boolean loginResult = user.login(user.getUser_account(), user.getUser_password());
                if (loginResult){
                    JOptionPane.showMessageDialog(touristPanel,"登陆成功，即将打开游客页面"
                            ,"登陆成功",JOptionPane.INFORMATION_MESSAGE);
                    JFrame tourist = new TouristExhibition(user.getUser_account()).openTouristPanel();
                    tourist.setBounds(frame.getX(),frame.getY(),frame.getWidth(),frame.getHeight());
                }else{
                    JOptionPane.showMessageDialog(touristPanel,"似乎出了一点小问题，请检查你的账号和密码是否错误"
                            ,"登陆成功",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPane.add(loginButton);
        box.add(buttonPane);
        touristPanel.add(box);
        layeredPane.moveToBack(touristPanel);
    }
    public void registered(){
        // 先清除组件上原有的东西，防止我们每一次触发事件都产生新的组件
        registered.removeAll();
        // 创建一个按钮组件
        JButton registered = new JButton("注册");
        Box box = Box.createVerticalBox();
        JLabel nameLabel = new JLabel("请输入你的用户名：");
        JTextField name = new JTextField("请输入你的用户名");
        JLabel accountLabel = new JLabel("请输入你的账号：");
        JTextField account = new JTextField("请输入你的账号");
        JLabel passwordLabel = new JLabel("请输入你的密码：");
        JTextField password = new JTextField("请输入你的账户密码");
        box.add(nameLabel);
        box.add(name);
        box.add(accountLabel);
        box.add(account);
        box.add(passwordLabel);
        box.add(password);
        JPanel buttonPane = new JPanel();
        buttonPane.add(registered);
        // 为注册按钮添加事件
        registered.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Users user = getRegisteredUser(account, password);
                user.setUser_name(name.getText());
                boolean registeredResult = user.registered(user.getUser_name(), user.getUser_account()
                        , user.getUser_password());
                if (registeredResult){
                    JOptionPane.showMessageDialog(MainExhibition.this.registered
                            ,"您已经注册成功，请进入游客页面登录吧！","注册成功"
                            ,JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(MainExhibition.this.registered
                            ,"您注册的账号账号已经存在，请您重新注册","警告"
                            ,JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        box.add(buttonPane);
        this.registered.add(box);
        layeredPane.moveToBack(this.registered);
    }
    // 获得用户对象，设置相应的属性值
    public Users getRegisteredUser(JTextField account, JTextField password){
        Users user = new Users();
        user.setUser_account(account.getText());
        user.setUser_password(password.getText());
        return user;
    }

}
