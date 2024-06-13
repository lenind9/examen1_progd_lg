package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements IBookService {

    @Inject
    private EntityManager em;

    @Override
    public Book findById(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("select b from Book b order by id asc", Book.class)
                .getResultList();
    }

    @Override
    public Book insert(Book book) {
        try{
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
            return book;
        }catch (Exception e){
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public Book update(Book book) {
        try{
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
        return book;
    }

    @Override
    public void delete(Integer id) {
        try{
            em.getTransaction().begin();
            em.remove(this.findById(id));
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }
}
