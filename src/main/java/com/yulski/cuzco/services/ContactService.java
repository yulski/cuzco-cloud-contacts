package com.yulski.cuzco.services;

import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactService extends Service<Contact> {

    protected static final Logger logger = LoggerFactory.getLogger(ContactService.class.getCanonicalName());

    @Override
    public Contact getOne(int id) {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s WHERE %s = ? LIMIT 1",
                SCHEMA, Contact.Contract.TABLE_NAME, Contact.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("ContactService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            boolean hasResult = resultSet.next();
            if(!hasResult) {
                logger.info("Query returned empty result set");
                return null;
            }
            return Contact.builder()
                    .id(id)
                    .name(resultSet.getString(Contact.Contract.NAME_COLUMN))
                    .phoneNumber(resultSet.getString(Contact.Contract.PHONE_NUMBER_COLUMN))
                    .user(new UserService().getOne(resultSet.getInt(Contact.Contract.USER_ID_COLUMN)))
                    .build();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Contact> getAll() {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s", SCHEMA, Contact.Contract.TABLE_NAME);
        List<Contact> allContacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            logger.info("ContactService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            UserService userService = new UserService();
            boolean hasResult = resultSet.next();
            if(!hasResult) {
                logger.info("Query returned empty result set");
                return null;
            } else {
                do {
                    allContacts.add(Contact.builder()
                            .id(resultSet.getInt(Contact.Contract.ID_COLUMN))
                            .name(resultSet.getString(Contact.Contract.NAME_COLUMN))
                            .phoneNumber(resultSet.getString(Contact.Contract.PHONE_NUMBER_COLUMN))
                            .user(userService.getOne(resultSet.getInt(Contact.Contract.USER_ID_COLUMN)))
                            .build());
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return allContacts;
    }

    @Override
    public int create(Contact contact) {
        Connection connection = db.getConnection();
        String sql = String.format("INSERT INTO \"%s\".%s (%s, %s, %s) VALUES(?,?,?)",
                SCHEMA, Contact.Contract.TABLE_NAME, Contact.Contract.NAME_COLUMN, Contact.Contract.PHONE_NUMBER_COLUMN,
                Contact.Contract.USER_ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setInt(3, contact.getUser().getId());
            logger.info("ContactService executing query: '" + sql + "'");
            return handleInsert(statement);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return UPDATE_FAILURE;
    }

    @Override
    public boolean update(Contact contact) {
        Connection connection = db.getConnection();
        String sql = String.format("UPDATE \"%s\".%s SET %s = ?, %s = ? WHERE %s = ?",
                SCHEMA, Contact.Contract.TABLE_NAME, Contact.Contract.NAME_COLUMN, Contact.Contract.PHONE_NUMBER_COLUMN,
                Contact.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setInt(3, contact.getId());
            logger.info("ContactService executing query: '" + sql + "'");
            return statement.executeUpdate() == 1;
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        Connection connection = db.getConnection();
        String sql = String.format("DELETE FROM \"%s\".%s WHERE %s = ?", SCHEMA, Contact.Contract.TABLE_NAME,
                Contact.Contract.ID_COLUMN);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("ContactService executing query: '" + sql + "'");
            return statement.executeUpdate() == 1;
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public List<Contact> getContactsForUser(User user) {
        Connection connection = db.getConnection();
        String sql = String.format("SELECT * FROM \"%s\".%s WHERE %s = ?", SCHEMA, Contact.Contract.TABLE_NAME,
                User.Contract.ID_COLUMN);
        List<Contact> userContacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getId());
            logger.info("ContactService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            boolean hasResult = resultSet.next();
            if(!hasResult) {
                logger.info("Query returned empty result set");
                return null;
            } else {
                do {
                    userContacts.add(Contact.builder()
                            .id(resultSet.getInt(Contact.Contract.ID_COLUMN))
                            .name(resultSet.getString(Contact.Contract.NAME_COLUMN))
                            .phoneNumber(resultSet.getString(Contact.Contract.PHONE_NUMBER_COLUMN))
                            .user(user)
                            .build());
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return userContacts;
    }
}
