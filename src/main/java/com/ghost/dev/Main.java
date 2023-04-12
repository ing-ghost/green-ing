package com.ghost.dev;

import com.ghost.dev.network.Server;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
