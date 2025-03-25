package com.example.service;

public interface BaseService<T> {
    Boolean save(T object);

    Boolean update(T object);

    Boolean delete(String guid);
}
