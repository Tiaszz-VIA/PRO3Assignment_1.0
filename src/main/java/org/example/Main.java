package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import via.pro3.SlaughterhouseServiceImpl;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(50051)
                .addService(new SlaughterhouseServiceImpl())
                .build()
                .start();

        System.out.println("Server started, listening on " + server.getPort());
        server.awaitTermination();
    }
}
