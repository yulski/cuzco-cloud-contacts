package com.yulski.cuzco.services;

import com.yulski.cuzco.db.Db;
import com.yulski.cuzco.models.Model;
import com.yulski.cuzco.util.Env;

import java.util.List;

public interface Service<T extends Model> {

    String SCHEMA = Env.getDbSchema();

    Db db = new Db();

    T getOne(int id);

    List<T> getAll();

    boolean create(T t);

    boolean update(T t);

    boolean delete(int id);

}
