package toolclass;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.Vector;

/**
 * @author FOLDN
 */
public class Users {
    private int user_id;
    private String user_name;
    private String user_account;
    private String user_password;
    private String user_type;
    private Timestamp user_start;
    public Users(){};
    public Users(int user_id, String user_name, String user_account, String user_password, String user_type, Timestamp user_start) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_account = user_account;
        this.user_password = user_password;
        this.user_type = user_type;
        this.user_start = user_start;
    }
    // 定义一个登录方法
    public boolean login(String user_account,String user_password){
        Connection connection = null;
        PreparedStatement preparedStatement =null;
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql = "select * from users where user_account = ? and user_password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user_account);
            preparedStatement.setString(2,user_password);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(preparedStatement,connection);
        }
        return false;
    }
    // 定义一个注册方法
    public boolean registered(String user_name,String user_account,String user_password){
        JdbcTemplate template = new JdbcTemplate(DruidJDBCUtils.getDataSource());
        // 再插入前应该查询一下表中是否有相同的账号
        boolean loginResult = login(user_account, user_password);
        if (loginResult){
            System.out.println("该账号已存在");
            return false;
        }
        // 定义插入语句
        String sql = "insert into users (user_name,user_account,user_password) value (?,?,?)";
        int result = template.update(sql, user_name, user_account, user_password);
        if(result != 0){
            return true;
        }else {
            return false;
        }

    }
    // 定义一个方法从数据库中获得内容，添加进Vector集合中
    public Vector getData(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Vector data = new Vector();
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql = "select * from users";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Vector user = new Vector();
                user.addElement(resultSet.getInt("user_id"));
                user.addElement(resultSet.getString("user_name"));
                user.addElement(resultSet.getString("user_account"));
                user.addElement(resultSet.getString("user_password"));
                user.addElement(resultSet.getString("user_type"));
                user.addElement(resultSet.getTimestamp("user_start"));
                data.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(resultSet,preparedStatement,connection);
            return data;
        }
    }
    // 定义一个方法根据账户判断是否是管理员
    public boolean isManage(String user_account){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql ="select user_type from users where user_account = ? and user_type = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user_account);
            preparedStatement.setString(2,"管理员");
            resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            if (result){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(resultSet,preparedStatement,connection);
        }
        return false;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Timestamp getUser_start() {
        return user_start;
    }

    public void setUser_start(Timestamp user_start) {
        this.user_start = user_start;
    }

    @Override
    public String toString() {
        return user_name;
    }
}
