package dev.serathiuk.server.chord.server;

public record ChordServerConfig(String remoteHost, int remotePort, String localHost, int localPort, int numberOfBitsKey) {
}
