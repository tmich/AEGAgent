package it.aeg2000srl.aegagent.core;

/**
 * Created by Tiziano on 24/09/2015.
 */
public interface IRepository<T> {
    T getById(long id);
    long add(T t);
    void edit(T t);
    void remove(T t);
    Iterable<T> getAll();
    long size();
}
