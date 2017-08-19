package com.yulski.cuzco.services;

import com.yulski.cuzco.models.Contact;
import com.yulski.cuzco.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService implements Service<Contact> {

    protected static final Logger logger = LoggerFactory.getLogger(ContactService.class.getCanonicalName());

    @Override
    public Contact getOne(int id) {
        Connection connection = db.getConnection();
        String sql = "SELECT * FROM " + Contact.Contract.TABLE_NAME +
                " WHERE " + Contact.Contract.ID_COLUMN + " = ? LIMIT 1";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            logger.info("ContactService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
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
        String sql = "SELECT * FROM " + User.Contract.TABLE_NAME;
        List<Contact> allContacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            logger.info("ContactService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            UserService userService = new UserService();
            while(resultSet.next()) {
                allContacts.add(Contact.builder()
                        .id(resultSet.getInt(Contact.Contract.ID_COLUMN))
                        .name(resultSet.getString(Contact.Contract.NAME_COLUMN))
                        .phoneNumber(resultSet.getString(Contact.Contract.PHONE_NUMBER_COLUMN))
                        .user(userService.getOne(resultSet.getInt(Contact.Contract.USER_ID_COLUMN)))
                        .build());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return allContacts;
    }

    @Override
    public boolean create(Contact contact) {
        Connection connection = db.getConnection();
        String sql = "INSERT INTO " + Contact.Contract.TABLE_NAME + "(" + Contact.Contract.NAME_COLUMN + ", " +
                Contact.Contract.PHONE_NUMBER_COLUMN + ", " + Contact.Contract.USER_ID_COLUMN + ") VALUES(?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setInt(3, contact.getUser().getId());
            logger.info("ContactService executing query: '" + sql + "'");
            return statement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(Contact contact) {
        Connection connection = db.getConnection();
        String sql = "UPDATE " + Contact.Contract.TABLE_NAME + " SET " + Contact.Contract.NAME_COLUMN + " = ?, " +
                Contact.Contract.PHONE_NUMBER_COLUMN + " = ? WHERE " + Contact.Contract.ID_COLUMN + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setInt(3, contact.getId());
            logger.info("ContactService executing query: '" + sql + "'");
            return statement.execute();
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(Contact contact) {
        Connection connection = db.getConnection();
        String sql = "DELETE FROM " + Contact.Contract.TABLE_NAME + " WHERE " + Contact.Contract.ID_COLUMN + " = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, contact.getId());
            logger.info("ContactService executing query: '" + sql + "'");
            return statement.execute();
        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public List<Contact> getContactsForUser(User user) {
        Connection connection = db.getConnection();
        String sql = "SELECT * FROM  " + Contact.Contract.TABLE_NAME +
                " WHERE " + Contact.Contract.USER_ID_COLUMN + " = ?";
        List<Contact> userContacts = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getId());
            logger.info("ContactService executing query: '" + sql + "'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                userContacts.add(Contact.builder()
                        .id(resultSet.getInt(Contact.Contract.ID_COLUMN))
                        .name(resultSet.getString(Contact.Contract.NAME_COLUMN))
                        .phoneNumber(resultSet.getString(Contact.Contract.PHONE_NUMBER_COLUMN))
                        .user(user)
                        .build());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return userContacts;
    }
}
