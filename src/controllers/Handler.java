package controllers;

public interface Handler <T> {
    T handle();
    T parse();
    boolean validate();

}
