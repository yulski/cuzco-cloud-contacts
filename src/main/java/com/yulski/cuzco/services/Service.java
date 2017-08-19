package com.yulski.cuzco.services;

import com.yulski.cuzco.db.Db;

import java.util.List;

public interface Service<T> {

   Db db = new Db();

    T getOne(int id);

    List<T> getAll();

    boolean create(T t);

    boolean update(T t);

    boolean delete(T t);

}
