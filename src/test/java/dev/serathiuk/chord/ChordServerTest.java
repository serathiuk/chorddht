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

    @Test
    public void testNetworkWithFiveNodes() throws Exception {
        var port3 = 6663; //6
        var port2 = 6662; //10
//        var port5 = 6665; //13
        var port1 = 6661; //18
        var port4 = 6664; //20

        // Add First Server
        step1IntegrationTest(port1);

        // Add Second Server
        step2IntegrationTest(port2, port1);

        // Add Third Server
        step3IntegrationTest(port3, port1, port2);

        // Add Fourth Server
        step4IntegrationTest(port4, port1, port2, port3);

//
//        createServer(ChordServerConfigBuilder.aChordServerConfig()
//                .withLocalHost(LOCALHOST)
//                .withLocalPort(port5)
//                .withRemoteHost(LOCALHOST)
//                .withRemotePort(port1)
//                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
//                .build());
//
//        Thread.sleep(10000);
//
//        // port3 -> port2 -> port5 -> port1 -> port4 -> port3
//
//        // Check port 1
//        try(var stub = new ChordGrpcStub(LOCALHOST, port1)) {
//            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());
//
//            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNode().getId());
//            assertEquals(LOCALHOST, nodeData.getNode().getHost());
//            assertEquals(port1, nodeData.getNode().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port4), nodeData.getNodeSuccessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
//            assertEquals(port4, nodeData.getNodeSuccessor().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port5), nodeData.getNodePredecessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
//            assertEquals(port5, nodeData.getNodePredecessor().getPort());
//        }
//
//        // Check port 2
//        try(var stub = new ChordGrpcStub(LOCALHOST, port2)) {
//            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());
//
//            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNode().getId());
//            assertEquals(LOCALHOST, nodeData.getNode().getHost());
//            assertEquals(port2, nodeData.getNode().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port5), nodeData.getNodeSuccessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
//            assertEquals(port5, nodeData.getNodeSuccessor().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNodePredecessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
//            assertEquals(port3, nodeData.getNodePredecessor().getPort());
//        }
//
//        // Check port3
//        try(var stub = new ChordGrpcStub(LOCALHOST, port3)) {
//            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());
//
//            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNode().getId());
//            assertEquals(LOCALHOST, nodeData.getNode().getHost());
//            assertEquals(port3, nodeData.getNode().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodeSuccessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
//            assertEquals(port2, nodeData.getNodeSuccessor().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port4), nodeData.getNodePredecessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
//            assertEquals(port4, nodeData.getNodePredecessor().getPort());
//        }
//
//        // Check node 4
//        try(var stub = new ChordGrpcStub(LOCALHOST, port4)) {
//            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());
//
//            assertEquals(keyService.hash(LOCALHOST, port4), nodeData.getNode().getId());
//            assertEquals(LOCALHOST, nodeData.getNode().getHost());
//            assertEquals(port4, nodeData.getNode().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNodeSuccessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
//            assertEquals(port3, nodeData.getNodeSuccessor().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodePredecessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
//            assertEquals(port1, nodeData.getNodePredecessor().getPort());
//        }
//
//        // Check port 5
//        try(var stub = new ChordGrpcStub(LOCALHOST, port5)) {
//            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());
//
//            assertEquals(keyService.hash(LOCALHOST, port5), nodeData.getNode().getId());
//            assertEquals(LOCALHOST, nodeData.getNode().getHost());
//            assertEquals(port5, nodeData.getNode().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodeSuccessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
//            assertEquals(port1, nodeData.getNodeSuccessor().getPort());
//
//            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodePredecessor().getId());
//            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
//            assertEquals(port2, nodeData.getNodePredecessor().getPort());
//        }

    }

    private void step4IntegrationTest(int port4, int port1, int port2, int port3) throws Exception {
        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port4)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        // Check first server
        try(var stub = new ChordGrpcStub(LOCALHOST, port1)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port4), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port4, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port2, nodeData.getNodePredecessor().getPort());
        }

        // Check second server
        try(var stub = new ChordGrpcStub(LOCALHOST, port2)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port1, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port3, nodeData.getNodePredecessor().getPort());
        }

        // Check third server
        try(var stub = new ChordGrpcStub(LOCALHOST, port3)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port2, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port4), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port4, nodeData.getNodePredecessor().getPort());
        }

        // Check fourth server
        try(var stub = new ChordGrpcStub(LOCALHOST, port4)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port4), nodeData.getNode().getId());
            assertEquals(LOCALHOST, nodeData.getNode().getHost());
            assertEquals(port4, nodeData.getNode().getPort());

            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port3, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port1, nodeData.getNodePredecessor().getPort());
        }
    }

    private void step3IntegrationTest(int port3, int port1, int port2) throws Exception {
        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port3)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        // 3 -> 2 -> 1

        // Check first server
        try(var stub = new ChordGrpcStub(LOCALHOST, port1)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port3, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port2, nodeData.getNodePredecessor().getPort());
        }

        // Check second server
        try(var stub = new ChordGrpcStub(LOCALHOST, port2)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port1, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port3, nodeData.getNodePredecessor().getPort());
        }

        // Check third server
        try(var stub = new ChordGrpcStub(LOCALHOST, port3)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port3), nodeData.getNode().getId());
            assertEquals(LOCALHOST, nodeData.getNode().getHost());
            assertEquals(port3, nodeData.getNode().getPort());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port2, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port1, nodeData.getNodePredecessor().getPort());
        }
    }

    private void step2IntegrationTest(int port2, int port1) throws Exception {
        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port2)
                .withRemoteHost(LOCALHOST)
                .withRemotePort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        // Check first server
        try(var stub = new ChordGrpcStub(LOCALHOST, port1)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port2, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port2), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port2, nodeData.getNodePredecessor().getPort());
        }

        // Check second server
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
    }

    private void step1IntegrationTest(int port1) throws Exception {
        createServer(ChordServerConfigBuilder.aChordServerConfig()
                .withLocalHost(LOCALHOST)
                .withLocalPort(port1)
                .withNumberOfBitsKey(NUMBER_OF_BITS_KEY)
                .build());

        // Check first server
        try(var stub = new ChordGrpcStub(LOCALHOST, port1)) {
            var nodeData = stub.getStub().getNodeData(Empty.getDefaultInstance());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNode().getId());
            assertEquals(LOCALHOST, nodeData.getNode().getHost());
            assertEquals(port1, nodeData.getNode().getPort());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodeSuccessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodeSuccessor().getHost());
            assertEquals(port1, nodeData.getNodeSuccessor().getPort());

            assertEquals(keyService.hash(LOCALHOST, port1), nodeData.getNodePredecessor().getId());
            assertEquals(LOCALHOST, nodeData.getNodePredecessor().getHost());
            assertEquals(port1, nodeData.getNodePredecessor().getPort());
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