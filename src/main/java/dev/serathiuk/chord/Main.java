package dev.serathiuk.chord;

import dev.serathiuk.chord.server.ChordNode;
import dev.serathiuk.chord.server.ChordServer;
import dev.serathiuk.chord.server.ChordServerConfigBuilder;
import dev.serathiuk.chord.server.GrpcChordNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static ExecutorService executorService;
    private static ChordNode localNode;

    public static void main(String[] args) {

        System.out.println("Welcome to the Serathiuk's Chord Network. ");
        System.out.println("------------------------------------------");
        System.out.println("Please, select the operation mode: ");

        while (true) {
            System.out.println("""
                    ---------------------------------------------------------------
                    | 1 - Start Chord | 3 - Put a Key | 5 - Show node info | 
                    | 2 - Stop  Chord | 4 - Get a Key | 9 - Exit           |
                    ---------------------------------------------------------------
                    """);

            switch (readInt("Option: ", 666)) {
               case 1 -> startChord();
               case 2 -> stopChord();
               case 3 -> putKey();
               case 4 -> getKey();
               case 5 -> showNodeInfo();
               case 9 -> exit();
               default -> System.out.println("Invalid option. Please, try again.");
            }
        }
    }

    public static void startChord() {
        var localHost = readString("Local host (default \"localhost\"): ", "localhost");
        var localPort = readInt("Local port (default \"8000\"): ", 8000);
        var remoteHost = readString("Remote host (blank for root node): ", "");
        var remotePort = readInt("Remote port (0 for root node): ", 0);

        executorService  = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            logger.info("Executing server at {}:{}", localHost, localPort);

            var chordServer = new ChordServer(ChordServerConfigBuilder.aChordServerConfig()
                    .withLocalHost(localHost)
                    .withLocalPort(localPort)
                    .withRemoteHost(remoteHost)
                    .withRemotePort(remotePort)
                    .build());

            chordServer.run();
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) { }

        localNode = new GrpcChordNode(localHost, localPort);
    }

    public static void putKey() {
        if(localNode == null) {
            System.out.println("The Chord Network is not started.");
            return;
        }

        var key = readString("Key: ", "");
        var value = readString("Value: ", "");

        var putResponse = localNode.put(key, value);

        System.out.println("Key " + putResponse.getKey() + " stored in node " + putResponse.getNodeId() + " with value " + putResponse.getValue());

    }

    public static void getKey() {
        if(localNode == null) {
            System.out.println("The Chord Network is not started.");
            return;
        }

        var key = readString("Key: ", "");

        var getResponse = localNode.get(key);

        System.out.println("Key " + getResponse.getKey() + " found in node " + getResponse.getNodeId() + " with value " + getResponse.getValue());
    }

    public static void showNodeInfo() {
        if(localNode == null) {
            System.out.println("The Chord Network is not started.");
            return;
        }

        System.out.println("Node ID: " + localNode.getId());
        System.out.println("Successor: " + localNode.getSuccessor().getId());
        System.out.println("Predecessor: " + localNode.getPredecessor().getId());
    }

    public static void stopChord() {
        localNode = null;

        try {
            executorService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit() {
        stopChord();
        System.exit(0);
    }

    public static String readString(String label, String defaultValue) {
        System.out.print(label);

        var value = new Scanner(System.in).nextLine();
        if(value == null || value.isEmpty())
            return defaultValue;

        return value;
    }

    public static int readInt(String label, int defaultValue) {
        try {
            System.out.print(label);
            var txt =  new Scanner(System.in).nextLine();
            if(txt == null || txt.isEmpty()) {
                return defaultValue;
            }

            return Integer.parseInt(txt.trim());
        } catch (Exception e) {
            return readInt(label, defaultValue);
        }
    }

}
