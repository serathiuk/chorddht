package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.Empty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChordServerTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int NUMBER_OF_BITS_KEY = 5;

    private ExecutorService executor;
    private KeyService keyService;
    private List<ChordServer> chordServers;

    @BeforeEach
    public void setUp() {
        executor = Executors.newFixedThreadPool(10);
        keyService = new KeyService(NUMBER_OF_BITS_KEY);
        chordServers = new ArrayList<>();
    }


    @Test
    public void testNetworkWithOneNode() throws Exception {
        var port = 6661;

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        try(var stub = new ChordGrpcStub(LOCALHOST, port)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            var id = keyService.hash(LOCALHOST, port);

            assertEquals(keyService.hash(LOCALHOST, port), nodeData.getNode().getId());
            assertEquals(LOCALHOST, nodeData.getNode().getHost());
            assertEquals(port, nodeData.getNode().getPort());

            assertEquals(id, nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port, nodeData.getNodeSuccessor().getPort());

            assertEquals(id, nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port, nodeData.getNodePredecessor().getPort());
        }
    }

    @Test
    public void testNetworkWithTwoNodes() throws Exception {
        var port1 = 6661;
        var port2 = 6662;

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                    .withLocalHost(LOCALHOST)
                    .withLocalPort(port1)
                    .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                    .build());

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                    .withLocalHost(LOCALHOST)
                    .withLocalPort(port2)
                    .withRemoteHost(LOCALHOST)
                    .withRemotePort(port1)
                    .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                    .build());

        Thread.sleep(3000);

        // Check node 2
        try(var stub = new ChordGrpcStub(LOCALHOST, port2)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNode().getId());
            assertEquals(LOCALHOST, nodeData.getNode().getHost());
            assertEquals(port2, nodeData.getNode().getPort());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port1, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port1, nodeData.getNodePredecessor().getPort());
        }

        // Check node 1
        try(var stub = new ChordGrpcStub(LOCALHOST, port1)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNode().getId());
            assertEquals(LOCALHOST, nodeData.getNode().getHost());
            assertEquals(port1, nodeData.getNode().getPort());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port2, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port2, nodeData.getNodePredecessor().getPort());
        }
    }

    private void createServer(ChordServerConfig config) throws Exception {
        executor.execute(() -> {
            var chordServer = new ChordServer(config);
            chordServers.add(chordServer);
            chordServer.run();
        });
        Thread.sleep(1000);
    }

    @AfterEach
    public void tearDown() {
        chordServers.forEach(ChordServer::shutdownNow);
        executor.shutdownNow();
        chordServers.clear();
    }

}