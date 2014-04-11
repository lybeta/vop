package cn.skyliuyang.dao;

/**
 * Created by liuyang on 14-3-26.
 */
public interface BaseDao<T> {

    public boolean insert(T t);

    public boolean remove(String id);

    public boolean update(T t);

    public T get(String id);
}
