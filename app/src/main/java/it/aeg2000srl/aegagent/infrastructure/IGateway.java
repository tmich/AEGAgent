package it.aeg2000srl.aegagent.infrastructure;

/**
 * Created by Tiziano on 24/09/2015.
 */
public interface IGateway<T> {
    void persist(T t);
    T retrieve(long id);
}
