package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.*;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChordServer  extends ChordGrpc.ChordImplBase implements Runnable {
    private static final Logger logger = Logger.getLogger(ChordServer.class.getName());


    private Server server;
    private ScheduledExecutorService stabilizationExecutor;
    private NodeData[] fingerTable = new NodeData[Key.NUMBER_OF_BITS_KEY];
    private Node node;
    private Node successor;
    private Node predecessor;

    public ChordServer(String remoteHost, int remotePort, String localHost, int localPort) {
        if(!StringUtils.isEmpty(remoteHost) && remotePort > 0) {
            this.successor = Node.newBuilder()
                    .setId(Key.hash(remoteHost, remotePort))
                    .setHost(remoteHost)
                    .setPort(remotePort)
                    .build();
        }

        this.node = Node.newBuilder()
                .setId(Key.hash(localHost, localPort))
                .setHost(localHost)
                .setPort(localPort)
                .build();
    }

    @Override
    public void run() {
        try {
            server = Grpc.newServerBuilderForPort(node.getPort(), InsecureServerCredentials.create())
                    .addService(this)
                    .build()
                    .start();

            logger.info("Server started, listening on " + node.getHost()+":"+node.getPort());

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.err.println();
                    logger.warning("*** shutting down gRPC server since JVM is shutting down");
                    try {
                        server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "Error", e);
                    }
                    logger.warning("*** server shut down");
                    System.err.println();
                }
            });

            join();

            stabilizationExecutor = Executors.newSingleThreadScheduledExecutor();
            stabilizationExecutor.scheduleWithFixedDelay(() -> {
                stabilize();
                fixFingers();
            }, 500, 1000, TimeUnit.MILLISECONDS);

            server.awaitTermination();
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, "Error", e);
        }
    }

    @Override
    public void findSuccessor(NodeId request, StreamObserver<NodeData> responseObserver) {
        responseObserver.onNext(findSuccessor(request));
        responseObserver.onCompleted();
    }

    private NodeData findSuccessor(NodeId request) {
        var pred = findPredecessor(request);
        try(var chordStub = new ChordGrpcStub(pred.getNodeSuccessor())) {
            return chordStub.getStub().getNodeData(Empty.getDefaultInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private NodeData findPredecessor(NodeId request) {
        var n = getNodeData();

        while (!Key.isBetween(request.getId(), n.getNode().getId(), n.getNodeSuccessor().getId())) {
            n = closestPrecedingFinger(request.getId());
        }
        return n;
    }

    private NodeData closestPrecedingFinger(String id) {
        for (NodeData nodeData : fingerTable) {
            if (nodeData == null)
                continue;

            if (Key.isBetween(nodeData.getNode().getId(), node.getId(), id))
                return nodeData;
        }

        return getNodeData();
    }

    private void join() {
        if(successor != null) {
            predecessor = null;

            try(var chordStub = new ChordGrpcStub(successor)) {
                successor = chordStub.getStub().findSuccessor(NodeId.newBuilder()
                        .setId(node.getId())
                        .build()).getNode();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            predecessor = node;
            successor = node;
        }
    }

    @Override
    public void getNodeData(Empty request, StreamObserver<NodeData> responseObserver) {
        responseObserver.onNext(getNodeData());
        responseObserver.onCompleted();
    }

    @Override
    public void notify(Node request, StreamObserver<Empty> responseObserver) {
        if(predecessor == null || Key.isBetween(request.getId(), predecessor.getId(), node.getId())) {
            predecessor = request;
        }

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private void stabilize() {
        try(var chordStub = new ChordGrpcStub(successor)) {
            var successorPredecessor = chordStub.getStub().getNodeData(Empty.getDefaultInstance()).getNodePredecessor();
            if(successorPredecessor != null && Key.isBetween(successorPredecessor.getId(), node.getId(), successor.getId())) {
                successor = successorPredecessor;
            }
            chordStub.getStub().notify(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fixFingers() {
        try(var chordStub = new ChordGrpcStub(successor)) {
            var random = new Random();
            var indice = random.nextInt(fingerTable.length);
            var identificador = ChordUtil.calculateFTNode(node.getId(), indice, Key.NUMBER_OF_BITS_KEY);

            fingerTable[indice] = chordStub.getStub()
                    .findSuccessor(NodeId.newBuilder()
                    .setId(identificador)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private NodeData getNodeData() {
        var builder = NodeData.newBuilder();

        if(predecessor != null)
            builder.setNodePredecessor(predecessor);

        if(successor != null)
            builder.setNodeSuccessor(successor);

        builder.setNode(node);

        return builder.build();
    }

}