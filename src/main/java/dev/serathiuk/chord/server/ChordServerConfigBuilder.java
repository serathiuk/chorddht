package dev.serathiuk.chord.server;public final class ChordServerConfigBuilder {
    private String remoteHost;
    private int remotePort;
    private String localHost;
    private int localPort;
    private int numberOfBitsKey;

    private ChordServerConfigBuilder() {
    }

    public static ChordServerConfigBuilder aChordServerConfig() {
        return new ChordServerConfigBuilder();
    }

    public ChordServerConfigBuilder withRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
        return this;
    }

    public ChordServerConfigBuilder withRemotePort(int remotePort) {
        this.remotePort = remotePort;
        return this;
    }

    public ChordServerConfigBuilder withLocalHost(String localHost) {
        this.localHost = localHost;
        return this;
    }

    public ChordServerConfigBuilder withLocalPort(int localPort) {
        this.localPort = localPort;
        return this;
    }

    public ChordServerConfigBuilder withNumberOfBitsKey(int numberOfBitsKey) {
        this.numberOfBitsKey = numberOfBitsKey;
        return this;
    }

    public ChordServerConfig build() {
        return new ChordServerConfig(remoteHost, remotePort, localHost, localPort, numberOfBitsKey);
    }
}
