package dev.serathiuk.chord.client;

public class ChordClient implements Runnable {

    private String host;
    private int port;

    public ChordClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

    }
}
