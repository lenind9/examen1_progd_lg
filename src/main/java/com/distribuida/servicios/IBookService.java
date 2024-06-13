package com.distribuida.servicios;

import com.distribuida.db.Book;

import java.util.List;

public interface IBookService {
    Book findById(Integer id);
    List<Book> findAll();
    Book insert(Book book);
    Book update(Book book);
    void delete(Integer id);
}
