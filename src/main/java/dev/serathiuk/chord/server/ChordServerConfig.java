package dev.serathiuk.chord.server;

public record ChordServerConfig(String remoteHost, int remotePort, String localHost, int localPort, int numberOfBitsKey) {
}
