package dev.serathiuk.chord;

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
    private List<ChordServer> chordServers;

    @BeforeEach
    public void setUp() {
        executor = Executors.newFixedThreadPool(10);
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

        try(var  node = new GrpcChordNode(LOCALHOST, port)) {
            var id = Key.hash(LOCALHOST, port);
            assertEquals(id, node.getId());
            assertEquals(LOCALHOST, node.getHost());
            assertEquals(port, node.getPort());

            var successor = node.getSuccessor();
            assertEquals(id, successor.getId());
            assertEquals(LOCALHOST, successor.getHost());
            assertEquals(port, successor.getPort());

            var predecessor = node.getPredecessor();
            assertEquals(id, predecessor.getId());
            assertEquals(LOCALHOST, predecessor.getHost());
            assertEquals(port, predecessor.getPort());
        }
    }

    @Test
    public void testNetworkWithTwoeNode() throws Exception {
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

        try(var  node1 = new GrpcChordNode(LOCALHOST, port1);
            var  node2 = new GrpcChordNode(LOCALHOST, port2)) {

            var id1 = Key.hash(LOCALHOST, port1);
            var id2 = Key.hash(LOCALHOST, port2);

            var node1Successor = node1.getSuccessor();
            var node1Predecessor = node1.getPredecessor();
            var node2Successor = node2.getSuccessor();
            var node2Predecessor = node2.getPredecessor();

            assertEquals(id1, node1.getId());
            assertEquals(LOCALHOST, node1.getHost());
            assertEquals(port1, node1.getPort());

            assertEquals(id2, node2.getId());
            assertEquals(LOCALHOST, node1.getHost());
            assertEquals(port2, node2.getPort());

            assertEquals(node1.getId(), node2Successor.getId());
            assertEquals(node1.getId(), node2Predecessor.getId());

            assertEquals(node2.getId(), node1Successor.getId());
            assertEquals(node2.getId(), node1Predecessor.getId());
        }
    }


    private void createServer(ChordServerConfig config) throws Exception {
        executor.execute(() -> {
            var chordServer = new ChordServer(config);
            chordServers.add(chordServer);
            chordServer.run();
        });
        Thread.sleep(3000);
    }

    @AfterEach
    public void tearDown() {
        chordServers.forEach(ChordServer::shutdownNow);
        executor.shutdownNow();
        chordServers.clear();
    }

}