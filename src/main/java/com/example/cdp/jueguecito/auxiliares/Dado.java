package com.example.cdp.jueguecito.auxiliares;

/**
 * Created by CDP on 06/02/2016.
 */
public class Dado {

    public static int tirar() {
        return (int)(6 * Math.random()) + 1;
    }
}
