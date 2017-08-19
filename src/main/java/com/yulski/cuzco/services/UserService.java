package com.yulski.cuzco.services;

import com.yulski.cuzco.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Service<User> {

    protected static final Logger logger = LoggerFactory.getLogger(UserService.class.getCanonicalName());

    @Override
    public User getOne(int id) {
        Connection connection = db.getConnection();
        String sql = "SELECT * FROM " + User.Contract.TABLE_NAME +
                " WHERE " + User.Contract.ID_COLUMN + " = ? LIMIT 1";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("UserService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
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

    @Override
    public List<User> getAll() {
        Connection connection = db.getConnection();
        String sql = "SELECT * FROM " + User.Contract.TABLE_NAME;
        List<User> allUsers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            logger.info("UserService executing query: '" + sql + "'");
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
    public boolean create(User user) {
        Connection connection = db.getConnection();
        String sql = "INSERT INTO " + User.Contract.TABLE_NAME + "(" + User.Contract.EMAIL_COLUMN + ", " +
                User.Contract.PASSWORD_COLUMN + ") VALUES (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            logger.info("UserService executing query: '" + sql + "'");
            return statement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        Connection connection = db.getConnection();
        String sql = "UPDATE " + User.Contract.TABLE_NAME + " SET " + User.Contract.EMAIL_COLUMN + " = ?, " +
                User.Contract.PASSWORD_COLUMN + " = ? WHERE " + User.Contract.ID_COLUMN + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());
            logger.info("UserService executing query: '" + sql + "'");
            return statement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        Connection connection = db.getConnection();
        String sql = "DELETE FROM " + User.Contract.TABLE_NAME + " WHERE " + User.Contract.ID_COLUMN + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getId());
            logger.info("UserService executing query: '" + sql + "'");
            return statement.execute();
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
