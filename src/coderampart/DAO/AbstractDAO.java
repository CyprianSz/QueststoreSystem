package coderampart.dao;

public class AbstractDAO<T> {

    private ArrayList<T> objectList = new ArrayList<T>();

    public abstract ArrayList<T> readAll();
    public abstract void create(T object);
    public abstract void update(T object);
    public abstract void delete(T object);

}
