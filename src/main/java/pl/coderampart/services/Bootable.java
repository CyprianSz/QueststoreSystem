package pl.coderampart.services;

public interface Bootable<T> {
    public boolean start(T User);
}
