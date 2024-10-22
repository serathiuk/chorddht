package dev.serathiuk.server.chord.server;

public class Main {

    public static void main(String[] args) {
        print(6661);
        print(6662);
        print(6663);
        print(6664);
        print(6665);
    }

    private static void print(int port) {
        System.out.println(port+" = "+Key.hash("127.0.0.1", port));
    }

}