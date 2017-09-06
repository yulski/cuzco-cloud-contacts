package com.yulski.cuzco.services;

import com.yulski.cuzco.db.Db;
import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService extends Service<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class.getCanonicalName());

    public UserService(Db db) {
        super(db);
    }

    @Override
    public User getOne(int id) {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s WHERE %s = ? LIMIT 1",
                schema, User.Contract.TABLE_NAME, User.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("Executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            boolean hasResult = resultSet.next();
            if(!hasResult) {
                logger.info("Query returned empty result set");
                return null;
            }
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
                schema, User.Contract.TABLE_NAME, User.Contract.EMAIL_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            logger.info("Executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            boolean hasResult = resultSet.next();
            if(!hasResult) {
                logger.info("Query returned empty result set");
                return null;
            }
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
                schema, User.Contract.TABLE_NAME);
        List<User> allUsers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            logger.info("Executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            boolean hasResult = resultSet.next();
            if(!hasResult) {
                logger.info("Query returned empty result set");
                return null;
            } else {
                do {
                    allUsers.add(User.builder()
                            .id(resultSet.getInt(User.Contract.ID_COLUMN))
                            .email(resultSet.getString(User.Contract.EMAIL_COLUMN))
                            .password(resultSet.getString(User.Contract.PASSWORD_COLUMN))
                            .build());
                } while (resultSet.next());
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
                schema, User.Contract.TABLE_NAME, User.Contract.EMAIL_COLUMN, User.Contract.PASSWORD_COLUMN);
        try {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, hashedPassword);
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
                schema, User.Contract.TABLE_NAME, User.Contract.EMAIL_COLUMN, User.Contract.PASSWORD_COLUMN,
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
        String contactSql = String.format("DELETE FROM \"%s\".%s WHERE %s = ?",
                schema, Contact.Contract.TABLE_NAME, Contact.Contract.USER_ID_COLUMN);
        String userSql = String.format("DELETE FROM \"%s\".%s WHERE %s = ?",
                schema, User.Contract.TABLE_NAME, User.Contract.ID_COLUMN);
        try {
            connection.setAutoCommit(false);
            PreparedStatement contactStatement = connection.prepareStatement(contactSql);
            PreparedStatement userStatement = connection.prepareStatement(userSql);
            contactStatement.setInt(1, id);
            userStatement.setInt(1, id);
            logger.info("Executing query: '" + contactSql + "'");
            contactStatement.executeUpdate();
            logger.info("Executing query: '" + userSql + "'");
            boolean success = userStatement.executeUpdate() == 1;
            connection.commit();
            if(!success) {
                logger.error("Deleting user failed. Rolling back.");
                connection.rollback();
            }
            return success;
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean authenticate(String email, String password) {
        User user = getOneByEmail(email);
        return user != null && BCrypt.checkpw(password, user.getPassword());
    }
}
