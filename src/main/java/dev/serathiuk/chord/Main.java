package dev.serathiuk.chord;

public class Main {

    public static void main(String[] args) {
        var chordServer = new ChordServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost("localhost")
                .withLocalPort(6661)
                .build());

        chordServer.run();
    }

}