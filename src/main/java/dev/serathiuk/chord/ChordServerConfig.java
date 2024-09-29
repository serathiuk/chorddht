package dev.serathiuk.chord;

public record ChordServerConfig(String remoteHost, int remotePort, String localHost, int localPort, int numberOfBitsKey) {
}
