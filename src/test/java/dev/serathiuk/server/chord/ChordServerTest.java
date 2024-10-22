package dev.serathiuk.server.chord;

import dev.serathiuk.chord.server.*;
import dev.serathiuk.server.chord.server.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
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

            node.put("teste1", "valor1");
            node.put("teste2", "valor2");
            node.put("teste3", "valor3");

            var teste1 = node.get("teste1");
            var teste2 = node.get("teste2");
            var teste3 = node.get("teste3");

            assertEquals("teste1", teste1.getKey());
            assertEquals("valor1", teste1.getValue());
            assertEquals(node.getId(), teste1.getNodeId());

            assertEquals("teste2", teste2.getKey());
            assertEquals("valor2", teste2.getValue());
            assertEquals(node.getId(), teste2.getNodeId());

            assertEquals("teste3", teste3.getKey());
            assertEquals("valor3", teste3.getValue());
            assertEquals(node.getId(), teste3.getNodeId());
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

            node2.put("teste1", "valor1");
            node2.put("teste2", "valor2");
            node2.put("teste3", "valor3");
            node1.put("teste4", "valor4");
            node1.put("teste5", "valor5");
            node1.put("teste6", "valor6");

            var teste1 = node1.get("teste1");
            var teste2 = node1.get("teste2");
            var teste3 = node1.get("teste3");
            var teste4 = node1.get("teste4");
            var teste5 = node1.get("teste5");
            var teste6 = node1.get("teste6");

//        node2: 49-185, node1: 186-48
//        teste1 => 31 // no 1
//        teste2 => 251 //no 1
//        teste3 => 187 // no 1
//        teste4 => 159 // no 2
//        teste5 => 124 // no 2
//        teste6 => 73 // no 2

            assertEquals("teste1", teste1.getKey());
            assertEquals("valor1", teste1.getValue());
            assertEquals(node1.getId(), teste1.getNodeId());

            assertEquals("teste2", teste2.getKey());
            assertEquals("valor2", teste2.getValue());
            assertEquals(node1.getId(), teste2.getNodeId());

            assertEquals("teste3", teste3.getKey());
            assertEquals("valor3", teste3.getValue());
            assertEquals(node1.getId(), teste3.getNodeId());

            assertEquals("teste4", teste4.getKey());
            assertEquals("valor4", teste4.getValue());
            assertEquals(node2.getId(), teste4.getNodeId());

            assertEquals("teste5", teste5.getKey());
            assertEquals("valor5", teste5.getValue());
            assertEquals(node2.getId(), teste5.getNodeId());

            assertEquals("teste6", teste6.getKey());
            assertEquals("valor6", teste6.getValue());
            assertEquals(node2.getId(), teste6.getNodeId());
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

//        8080 => 169 -> node1
//        8081 => 43 -> node2
//        8082 => 232 -> node3
//        8083 => 131 -> node4
//        8084 => 35 -> node5
//        node5 (35-42) - node2 (43-130) - node4 (131-168) - node1 (169-231) - node3 (231-34) -> node5 (35-42)

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

            node1.put("teste1", "valor1");
            node1.put("teste2", "valor2");
            node2.put("teste3", "valor3");
            node2.put("teste4", "valor4");
            node3.put("teste5", "valor5");
            node3.put("teste6", "valor6");
            node4.put("teste7", "valor7");
            node4.put("teste8", "valor8");
            node5.put("teste9", "valor9");
            node5.put("teste10", "valor10");

            var teste1 = node5.get("teste1");
            var teste2 = node4.get("teste2");
            var teste3 = node3.get("teste3");
            var teste4 = node2.get("teste4");
            var teste5 = node1.get("teste5");
            var teste6 = node5.get("teste6");
            var teste7 = node4.get("teste7");
            var teste8 = node3.get("teste8");
            var teste9 = node2.get("teste9");
            var teste10 = node1.get("teste10");

            assertEquals("teste1", teste1.getKey());
            assertEquals("valor1", teste1.getValue());
            assertEquals(node3.getId(), teste1.getNodeId());

            assertEquals("teste2", teste2.getKey());
            assertEquals("valor2", teste2.getValue());
            assertEquals(node3.getId(), teste2.getNodeId());

            assertEquals("teste3", teste3.getKey());
            assertEquals("valor3", teste3.getValue());
            assertEquals(node1.getId(), teste3.getNodeId());

            assertEquals("teste4", teste4.getKey());
            assertEquals("valor4", teste4.getValue());
            assertEquals(node4.getId(), teste4.getNodeId());

            assertEquals("teste5", teste5.getKey());
            assertEquals("valor5", teste5.getValue());
            assertEquals(node2.getId(), teste5.getNodeId());

            assertEquals("teste6", teste6.getKey());
            assertEquals("valor6", teste6.getValue());
            assertEquals(node2.getId(), teste6.getNodeId());

            assertEquals("teste7", teste7.getKey());
            assertEquals("valor7", teste7.getValue());
            assertEquals(node4.getId(), teste7.getNodeId());

            assertEquals("teste8", teste8.getKey());
            assertEquals("valor8", teste8.getValue());
            assertEquals(node3.getId(), teste8.getNodeId());

            assertEquals("teste9", teste9.getKey());
            assertEquals("valor9", teste9.getValue());
            assertEquals(node2.getId(), teste9.getNodeId());

            assertEquals("teste10", teste10.getKey());
            assertEquals("valor10", teste10.getValue());
            assertEquals(node4.getId(), teste10.getNodeId());

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