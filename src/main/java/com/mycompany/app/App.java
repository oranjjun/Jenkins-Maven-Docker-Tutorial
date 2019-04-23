package com.mycompany.app;

/**
 * Hello world!
 */
public class App {

    private final String message = "Hello World!";

    public App() {
        throw new RuntimeException("Problem!");
    }

    public static void main(String[] args) {
        System.out.println(new App().getMessage());
    }

    private final String getMessage() {
        return message;
    }

}
