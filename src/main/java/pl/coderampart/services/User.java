package pl.coderampart.services;

import java.util.ArrayList;

public interface User<T> {
    T getLogged(String email, String password) throws Exception;
    ArrayList<T> readAll();
    void create(T User);
    void update(T User);
    void delete(T User);
}
