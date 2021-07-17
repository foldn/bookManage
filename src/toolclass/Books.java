package toolclass;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.Vector;

/**
 * @author FOLDN
 */
public class Books {


    private int books_id;
    private String books_name;
    private String books_author;
    private String books_type;

    private double books_price;
    private boolean books_isborrowed;
    private Timestamp books_date;
    public Books(){}
    public Books(int books_id, String books_name, String books_author, String books_type, double books_price
            , boolean books_isborrowed, Timestamp books_date) {
        this.books_id = books_id;
        this.books_name = books_name;
        this.books_author = books_author;
        this.books_type = books_type;
        this.books_price = books_price;
        this.books_isborrowed = books_isborrowed;
        this.books_date = books_date;
    }
    public Vector getData(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Vector data = new Vector();
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql = "select * from books";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Vector book = new Vector();
                book.addElement(resultSet.getInt("books_id"));
                book.addElement(resultSet.getString("books_name"));
                book.addElement(resultSet.getString("books_author"));
                book.addElement(resultSet.getString("books_type"));
                book.addElement(resultSet.getDouble("books_price"));
                book.addElement(resultSet.getBoolean("books_isborrowed"));
                book.addElement(resultSet.getTimestamp("books_date"));
                data.add(book);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(resultSet,preparedStatement,connection);
            return data;
        }
    }
    // 查询数据库中是否有这本书，如果有则返回true
    public boolean findBook(String books_name){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql = "select * from books where books_name = ? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,books_name);
            resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(resultSet,preparedStatement,connection);
        }
        return false;
    }
    // 查询数据库中该书是否被借走,没被借走则返回
    public boolean isBorrowed(String books_name){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DruidJDBCUtils.getConnection();
            String sql = "select books_isborrowed from books where books_name = ? and books_isborrowed = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,books_name);
            preparedStatement.setBoolean(2,true);
            resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidJDBCUtils.close(resultSet,preparedStatement,connection);
        }
        return false;
    }
    // 借书功能
    public boolean borrowBook(String books_name){
        boolean haveBook = findBook(books_name);
        // 如果书还在且没被借走
        if (haveBook && !isBorrowed(books_name)){
            JdbcTemplate template = new JdbcTemplate(DruidJDBCUtils.getDataSource());
            String sql = "update books set books_isborrowed = true where books_name = ?";
            int update = template.update(sql,books_name);
            if (update != 0){
                return true;
            }
            else {
                return false;
            }
        }else{
            return false;
        }
    }
    // 还书功能
    public boolean returnBook(String books_name){
        boolean haveBook = findBook(books_name);
        // 如果书还在
        if (haveBook && isBorrowed(books_name)){
            JdbcTemplate template = new JdbcTemplate(DruidJDBCUtils.getDataSource());
            String sql = "update books set books_isborrowed = false where books_name = ?";
            int update = template.update(sql,books_name);
            if (update != 0){
                return true;
            }
            else {
                return false;
            }
        }else{
            return false;
        }
    }
    // 添加书籍功能
    public boolean addBook(String booksFormat){
        JdbcTemplate template = new JdbcTemplate(DruidJDBCUtils.getDataSource());
        String[] split = booksFormat.split("-");
        String sql = "insert into books (books_name,books_author,books_type,books_price,books_isborrowed)" +
                "VALUES(?,?,?,?,false )";
        int update = template.update(sql,split[0],split[1],split[2],split[3]);
        if (update != 0){
            return true;
        }else {
            return false;
        }
    }
    // 删除书籍功能
    public boolean deleteBook(String books_name){
        JdbcTemplate template = new JdbcTemplate(DruidJDBCUtils.getDataSource());
        String sql = "delete from books where books_name = ?";
        int update = template.update(sql,books_name);
        if (update != 0){
            return true;
        }else {
            return false;
        }
    }
    public int getBooks_id() {
        return books_id;
    }

    public void setBooks_id(int books_id) {
        this.books_id = books_id;
    }

    public String getBooks_name() {
        return books_name;
    }

    public void setBooks_name(String books_name) {
        this.books_name = books_name;
    }

    public String getBooks_author() {
        return books_author;
    }

    public void setBooks_author(String books_author) {
        this.books_author = books_author;
    }

    public String getBooks_type() {
        return books_type;
    }

    public void setBooks_type(String books_type) {
        this.books_type = books_type;
    }

    public double getBooks_price() {
        return books_price;
    }

    public void setBooks_price(double books_price) {
        this.books_price = books_price;
    }

    public boolean isBooks_isborrowed() {
        return books_isborrowed;
    }

    public void setBooks_isborrowed(boolean books_isborrowed) {
        this.books_isborrowed = books_isborrowed;
    }

    public Timestamp getBooks_date() {
        return books_date;
    }

    public void setBooks_date(Timestamp books_date) {
        this.books_date = books_date;
    }

    @Override
    public String toString() {
        return books_name;
    }
}
