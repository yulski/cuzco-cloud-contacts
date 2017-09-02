package com.yulski.cuzco.services;

import com.yulski.cuzco.db.Db;
import com.yulski.cuzco.models.Model;
import com.yulski.cuzco.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class Service<T extends Model> {

    public static final int UPDATE_FAILURE = -1;

    public static final String SCHEMA = Env.getDbSchema();

    protected Db db;

    public Service(Db db) {
        this.db = db;
    }

    public abstract T getOne(int id);

    public abstract List<T> getAll();

    public abstract int create(T t);

    public abstract boolean update(T t);

    public abstract boolean delete(int id);

    protected final int handleInsert(PreparedStatement statement) {
        try {
            if (statement.executeUpdate() == 1) {
                ResultSet keys = statement.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            } else {
                return UPDATE_FAILURE;
            }
        } catch(SQLException e) {
            return UPDATE_FAILURE;
        }
    }

}
