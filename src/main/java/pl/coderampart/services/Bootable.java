package pl.coderampart.services;

import java.sql.SQLException;

public interface Bootable<T> {
    public boolean start(T User) throws SQLException;
}
