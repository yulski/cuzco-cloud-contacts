package com.yulski.cuzco.services;

import com.yulski.cuzco.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService extends Service<User> {

    protected static final Logger logger = LoggerFactory.getLogger(UserService.class.getCanonicalName());

    @Override
    public User getOne(int id) {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s WHERE %s = ? LIMIT 1",
                SCHEMA, User.Contract.TABLE_NAME, User.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("Executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return User.builder()
                    .id(id)
                    .email(resultSet.getString(User.Contract.EMAIL_COLUMN))
                    .password(resultSet.getString(User.Contract.PASSWORD_COLUMN))
                    .build();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public User getOneByEmail(String email) {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s WHERE %s = ? LIMIT 1",
                SCHEMA, User.Contract.TABLE_NAME, User.Contract.EMAIL_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            logger.info("Executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return User.builder()
                    .id(resultSet.getInt(User.Contract.ID_COLUMN))
                    .email(resultSet.getString(User.Contract.EMAIL_COLUMN))
                    .password(resultSet.getString(User.Contract.PASSWORD_COLUMN))
                    .build();
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s",
            SCHEMA, User.Contract.TABLE_NAME);
        List<User> allUsers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            logger.info("Executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                allUsers.add(User.builder()
                        .id(resultSet.getInt(User.Contract.ID_COLUMN))
                        .email(resultSet.getString(User.Contract.EMAIL_COLUMN))
                        .password(resultSet.getString(User.Contract.PASSWORD_COLUMN))
                        .build());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return allUsers;
    }

    @Override
    public int create(User user) {
        Connection connection = db.getConnection();
        String sql = String.format("INSERT INTO \"%s\".%s (%s, %s) VALUES (?, ?)",
                SCHEMA, User.Contract.TABLE_NAME, User.Contract.EMAIL_COLUMN, User.Contract.PASSWORD_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            logger.info("Executing query: '" + sql + "'");
            return handleInsert(statement);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return UPDATE_FAILURE;
    }

    @Override
    public boolean update(User user) {
        Connection connection = db.getConnection();
        String sql = String.format("UPDATE \"%s\".%s SET %s = ?, %s = ? WHERE %s = ?",
                SCHEMA, User.Contract.TABLE_NAME, User.Contract.EMAIL_COLUMN, User.Contract.PASSWORD_COLUMN,
                User.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());
            logger.info("Executing query: '" + sql + "'");
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        Connection connection = db.getConnection();
        String sql = String.format("DELETE FROM \"%s\".%s WHERE %s = ?",
                SCHEMA, User.Contract.TABLE_NAME, User.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("Executing query: '" + sql + "'");
            return statement.executeUpdate() == 1;
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean authenticate(String email, String password) {
        User user = getOneByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
