package cn.skyliuyang.service;

/**
 * Created by liuyang on 14-3-26.
 */
public interface BaseService<T> {

    public boolean create(T t);

    public boolean delete(String id);

    public boolean update(T t);

    public T get(String id);
}
