package com.glaserproject.flicks.MyObjects;

/**
 * Review Object
 */

public class Review {

    String name;
    String body;

    public Review(String name, String body) {
        this.name = name;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getName() {
        return name;
    }


}
