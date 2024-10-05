package dev.serathiuk.chord;

public class Main {

    public static void main(String[] args) {
        var keyService = new KeyService(5);
        print(6661, keyService);
        print(6662, keyService);
        print(6663, keyService);
        print(6664, keyService);
        print(6665, keyService);
    }

    private static void print(int port, KeyService keyService) {
        System.out.println(port+" = "+keyService.hash("127.0.0.1", port));
    }

}