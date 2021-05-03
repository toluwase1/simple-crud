package com.example.fb_demo.dao;

import com.example.fb_demo.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/fbdemo?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "toluwase";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +
            " (?, ?, ?);";

    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    public UserDAO() {

    }

    //METHOD CREATES JDBC CONNECTION WITH MYSQL DATABASE
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    //create or insert user
    public void insertUser (User user) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update user
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        //1. establish connection
        try (Connection connection = getConnection();
             //2 create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL)){
             preparedStatement.setString(1, user.getName());
             preparedStatement.setString(2, user.getEmail());
             preparedStatement.setString(3, user.getCountry());
             preparedStatement.setInt(4, user.getId());
            //3. Execute query or update query
             rowUpdated = preparedStatement.executeUpdate()>0;
        }

       return rowUpdated;
    }

    //select users by ID
    public User selectUser(int id) throws SQLException {
        User user = null;
        //1. establish connection
        try (Connection connection = getConnection();
             //2 create a statement using connection object
              PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            //3. Execute query or update query
            ResultSet rs = preparedStatement.executeQuery();
            //4 process the result set object
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    //select  all user
    public List<User> selectAllUser() throws SQLException {
        List<User> users = new ArrayList<>();
        //1. establish connection
        try (Connection connection = getConnection();
             //2 create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {

            //3. Execute query or update query
            ResultSet rs = preparedStatement.executeQuery();
            //4 process the result set object
            while (rs.next()) {
                int id = rs.getInt( "id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // delete user
    public boolean deleteUser (int id) throws SQLException {
        boolean rowDeleted;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);) {
            preparedStatement.setInt(1,id);
            rowDeleted = preparedStatement.executeUpdate()>0;
        }
        return rowDeleted;
    }

}
