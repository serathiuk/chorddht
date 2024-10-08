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

    private static final String LOCALHOST = "localhost";
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

        Thread.sleep(5000);

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

    @Test
    public void testNetworkWithFiveNodes() throws Exception {
        var port1 = 8080;
        var port2 = 8081;
        var port3 = 8082;
        var port4 = 8083;
        var port5 = 8084;

        var id1 = Key.hash(LOCALHOST, port1);
        var id2 = Key.hash(LOCALHOST, port2);
        var id3 = Key.hash(LOCALHOST, port3);
        var id4 = Key.hash(LOCALHOST, port4);
        var id5 = Key.hash(LOCALHOST, port5);

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        try(var node1 = new GrpcChordNode(LOCALHOST, port1)) {
            assertEquals(id1, node1.getId());
            assertEquals(LOCALHOST, node1.getHost());
            assertEquals(port1, node1.getPort());

            assertEquals(node1.getId(), node1.getSuccessor().getId());
            assertEquals(node1.getId(), node1.getPredecessor().getId());
        }

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port2)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        try(var node1 = new GrpcChordNode(LOCALHOST, port1);
            var node2 = new GrpcChordNode(LOCALHOST, port2)) {
            assertEquals(id2, node2.getId());
            assertEquals(LOCALHOST, node2.getHost());
            assertEquals(port2, node2.getPort());

            assertEquals(node2.getId(), node1.getSuccessor().getId());
            assertEquals(node2.getId(), node1.getPredecessor().getId());

            assertEquals(node1.getId(), node2.getSuccessor().getId());
            assertEquals(node1.getId(), node2.getPredecessor().getId());
        }

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port3)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());


        try(var node1 = new GrpcChordNode(LOCALHOST, port1);
            var node2 = new GrpcChordNode(LOCALHOST, port2);
            var node3 = new GrpcChordNode(LOCALHOST, port3)) {
            assertEquals(id3, node3.getId());
            assertEquals(LOCALHOST, node3.getHost());
            assertEquals(port3, node3.getPort());

            assertEquals(node3.getId(), node1.getSuccessor().getId());
            assertEquals(node2.getId(), node1.getPredecessor().getId());

            assertEquals(node1.getId(), node2.getSuccessor().getId());
            assertEquals(node3.getId(), node2.getPredecessor().getId());

            assertEquals(node2.getId(), node3.getSuccessor().getId());
            assertEquals(node1.getId(), node3.getPredecessor().getId());
        }

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port4)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        try(var node1 = new GrpcChordNode(LOCALHOST, port1);
            var node2 = new GrpcChordNode(LOCALHOST, port2);
            var node3 = new GrpcChordNode(LOCALHOST, port3);
            var node4 = new GrpcChordNode(LOCALHOST, port4)) {

            assertEquals(id4, node4.getId());
            assertEquals(LOCALHOST, node4.getHost());
            assertEquals(port4, node4.getPort());

            assertEquals(node3.getId(), node1.getSuccessor().getId());
            assertEquals(node4.getId(), node1.getPredecessor().getId());

            assertEquals(node4.getId(), node2.getSuccessor().getId());
            assertEquals(node3.getId(), node2.getPredecessor().getId());

            assertEquals(node2.getId(), node3.getSuccessor().getId());
            assertEquals(node1.getId(), node3.getPredecessor().getId());

            assertEquals(node1.getId(), node4.getSuccessor().getId());
            assertEquals(node2.getId(), node4.getPredecessor().getId());
        }

        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port5)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        try(var node1 = new GrpcChordNode(LOCALHOST, port1);
            var node2 = new GrpcChordNode(LOCALHOST, port2);
            var node3 = new GrpcChordNode(LOCALHOST, port3);
            var node4 = new GrpcChordNode(LOCALHOST, port4);
            var node5 = new GrpcChordNode(LOCALHOST, port5)) {

            assertEquals(id5, node5.getId());
            assertEquals(LOCALHOST, node5.getHost());
            assertEquals(port5, node5.getPort());

            //        8080 => 169 -> node1
//        8081 => 43 -> node2
//        8082 => 232 -> node3
//        8083 => 131 -> node4
//        8084 => 35 -> node5
//        node5 - node2 - node4 - node1 - node3 -> node5

            assertEquals(node3.getId(), node1.getSuccessor().getId());
            assertEquals(node4.getId(), node1.getPredecessor().getId());

            assertEquals(node4.getId(), node2.getSuccessor().getId());
            assertEquals(node5.getId(), node2.getPredecessor().getId());

            assertEquals(node5.getId(), node3.getSuccessor().getId());
            assertEquals(node1.getId(), node3.getPredecessor().getId());

            assertEquals(node1.getId(), node4.getSuccessor().getId());
            assertEquals(node2.getId(), node4.getPredecessor().getId());

            assertEquals(node2.getId(), node5.getSuccessor().getId());
            assertEquals(node3.getId(), node5.getPredecessor().getId());
        }
    }


    private void createServer(ChordServerConfig config) throws Exception {
        executor.execute(() -> {
            var chordServer = new ChordServer(config);
            chordServers.add(chordServer);
            chordServer.run();
        });
        Thread.sleep(2000);
    }

    @AfterEach
    public void tearDown() {
        chordServers.forEach(ChordServer::shutdownNow);
        executor.shutdownNow();
        chordServers.clear();
    }

}