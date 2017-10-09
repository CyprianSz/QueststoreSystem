package pl.coderampart.services;

import java.sql.SQLException;
import java.util.ArrayList;

public interface User<T> {
    T getLogged(String email, String password) throws Exception;
    ArrayList<T> readAll() throws SQLException;
    void create(T User) throws SQLException;
    void update(T User) throws SQLException;
    void delete(T User) throws SQLException;
}
