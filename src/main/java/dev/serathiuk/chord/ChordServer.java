package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.*;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChordServer  extends ChordGrpc.ChordImplBase implements Runnable {

    private static final Logger logger = Logger.getLogger(ChordServer.class.getName());

    private Server server;
    private ExecutorService executorService;
    private Node[] fingerTable;
    private Node node;
    private Node successor;
    private Node predecessor;
    private ChordServerConfig config;
    private boolean started = false;
    private Map<String, String> mapInformation = new HashMap<>();
    private KeyService keyService;

    public ChordServer(ChordServerConfig config) {
        this.config = config;

        var numberOfBitsKey = config.numberOfBitsKey() > 0 ? config.numberOfBitsKey() : 120;
        fingerTable = new Node[numberOfBitsKey];

        keyService = new KeyService(numberOfBitsKey);

        this.node = Node.newBuilder()
                .setId(keyService.hash(config.localHost(), config.localPort()))
                .setHost(config.localHost())
                .setPort(config.localPort())
                .build();

        if(!StringUtils.isEmpty(config.remoteHost()) && config.remotePort() > 0) {
            this.successor = Node.newBuilder()
                    .setId(keyService.hash(config.remoteHost(), config.remotePort()))
                    .setHost(config.remoteHost())
                    .setPort(config.remotePort())
                    .build();

            logger.info("Node "+node.getId()+" | Configure successor: "+ this.successor);
        }

        logger.info("Node "+node.getId()+" | Configure node: "+ this.node);
    }

    @Override
    public void run() {
        try {
            server = Grpc.newServerBuilderForPort(node.getPort(), InsecureServerCredentials.create())
                    .addService(this)
                    .build()
                    .start();

            logger.info("Server started, listening on " + node.getHost()+":"+node.getPort());

            join();

            executorService = Executors.newFixedThreadPool(1);
            executorService.execute(() -> {
                while(true) {
                    try {
                        Thread.sleep(500);
                        stabilize();
                        fixFingers();
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "Node "+node.getId()+" | Error", e);
                    }
                }
            });

            // Shutdown hook para fechar o servidor corretamente
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("** Servidor encerrado via shutdown hook **");
                server.shutdownNow();
            }));

            started = true;

            server.awaitTermination();
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, "Node "+node.getId()+" | Error", e);
        }
    }

    @Override
    public void findSuccessor(NodeId request, StreamObserver<Node> responseObserver) {
        responseObserver.onNext(findSuccessor(request));
        responseObserver.onCompleted();
    }

    private Node findSuccessor(NodeId request) {
        if(request.getId().equals(node.getId()))
            return node;

        var pred = findPredecessor(request);
        if(pred.getId().equals(node.getId()))
            return pred;

        try(var chordStub = new ChordGrpcStub(pred)) {
            return chordStub.getNodeData().getNodeSuccessor();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Node "+node.getId()+" | Error", e);
            throw new RuntimeException(e);
        }
    }


    private Node findPredecessor(NodeId request) {
        if(successor == null || StringUtils.isEmpty(successor.getId()))
            return node;

        var n = node;
        while (!keyService.isBetween(request.getId(), node.getId(), successor.getId(), true)) {
            n = closestPrecedingFinger(request.getId());
        }
        return n;
    }

    private Node closestPrecedingFinger(String id) {
        for (var i = fingerTable.length - 1; i >= 0; i--) {
            var n = fingerTable[i];
            if (n == null)
                continue;

            if (keyService.isBetween(n.getId(), node.getId(), id, false))
                return n;
        }

        return node;
    }

    private void join() {
        if(successor == null || StringUtils.isEmpty(successor.getId())) {
            successor = node;
            predecessor = node;
            return;
        }

        try(var chordStub = new ChordGrpcStub(successor)) {
            successor = chordStub.getStub().findSuccessor(NodeId.newBuilder()
                    .setId(node.getId())
                    .build());
            predecessor = null;

            chordStub.getStub().notify(node);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Node "+node.getId()+" | Error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getNodeData(Empty request, StreamObserver<NodeData> responseObserver) {
        responseObserver.onNext(getNodeData());
        responseObserver.onCompleted();
    }

    @Override
    public void notify(Node request, StreamObserver<Empty> responseObserver) {
        if(predecessor == null || StringUtils.isEmpty(predecessor.getId()) ||
                keyService.isBetween(request.getId(), predecessor.getId(), node.getId(), false)) {
            predecessor = request;
            logger.info("Node "+node.getId()+" | Atualizado predecessor de "+node.getId()+" para: "+predecessor);
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void put(Entry request, StreamObserver<PutResponse> responseObserver) {
        super.put(request, responseObserver);
    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        super.get(request, responseObserver);
    }

    public void stabilize() {
        if(successor == null || successor.getPort() <= 0)
            return;

        logger.info("Node "+node.getId()+" | Stabilizing node: " + node.toString());
        try(var chordStub = new ChordGrpcStub(successor)) {
            var x = chordStub.getNodeData().getNodePredecessor();

            if(x != null && keyService.isBetween(x.getId(), node.getId(), successor.getId(), false)) {
                successor = x;
                logger.info("Node "+node.getId()+" | New successor: " + successor);
            }

            chordStub.getStub().notify(node);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Node "+node.getId()+" | Error", e);
            throw new RuntimeException(e);
        }
    }

    public void fixFingers() {
        if(successor == null || successor.getPort() <= 0)
            return;

        var random = new Random();
        var indice = random.nextInt(fingerTable.length);
        var identificador = ChordUtil.calculateFTNode(node.getId(), indice+1, fingerTable.length);

        logger.info("Node "+node.getId()+" | Fixing finger: " + identificador);

        fingerTable[indice] = findSuccessor(NodeId.newBuilder()
                .setId(identificador)
                .build());

        logger.info("Node "+node.getId()+" | Fixed finger: " + identificador+ " Node: "+ fingerTable[indice].getId());
    }

    public NodeData getNodeData() {
        var builder = NodeData.newBuilder();

        if(predecessor != null)
            builder.setNodePredecessor(predecessor);

        if(successor != null)
            builder.setNodeSuccessor(successor);

        builder.setNode(node);

        return builder.build();
    }

    public boolean isStarted() {
        return started;
    }

    public void shutdownNow() {
        server.shutdownNow();
    }
}