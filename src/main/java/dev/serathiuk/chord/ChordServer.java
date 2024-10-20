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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChordServer  extends ChordGrpc.ChordImplBase implements Runnable {

    private ChordServerConfig config;
    private Server server;
    private ExecutorService executorServiceFixFinger;
    private ExecutorService executorServiceStabilize;

    private Map<String, String> mapInformation = new HashMap<>();
    private LocalChordNode node;

    private boolean started = false;

    public ChordServer(ChordServerConfig config) {
        this.config = config;

        node = new LocalChordNode(config.localHost(), config.localPort());
    }

    @Override
    public void run() {
        try {
            server = Grpc.newServerBuilderForPort(node.getPort(), InsecureServerCredentials.create())
                    .addService(this)
                    .keepAliveTime(60, TimeUnit.SECONDS)  // Tempo entre verificações de keepalive
                    .keepAliveTimeout(120, TimeUnit.SECONDS)
                    .build()
                    .start();

            System.out.println("Server started, listening on " + node.getHost()+":"+node.getPort());

            // Shutdown hook para fechar o servidor corretamente
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("** Servidor encerrado via shutdown hook **");
                server.shutdownNow();
            }));

            started = true;

            executorServiceStabilize = Executors.newFixedThreadPool(1);
            executorServiceStabilize.execute(() -> {
                while (!started)
                    Thread.onSpinWait();

                if(!StringUtils.isEmpty(config.remoteHost()) && config.remotePort() > 0) {
                    node.join(new GrpcChordNode(config.remoteHost(), config.remotePort()));
                }

                while(started) {
                    try {
                        Thread.sleep(300);
                        node.stabilize();
                    } catch (InterruptedException e) {
                        System.err.println( "Node "+node.getId());
                        e.printStackTrace();
                    }
                }
            });

            executorServiceFixFinger = Executors.newFixedThreadPool(1);
            executorServiceFixFinger.execute(() -> {
                if(!started) return;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println( "Node "+node.getId());
                    e.printStackTrace();
                }
                while(started) {
                    try {
                        Thread.sleep(500);
                        node.fixFingers();
                    } catch (InterruptedException e) {
                        System.err.println( "Node "+node.getId());
                        e.printStackTrace();
                    }
                }
            });

            server.awaitTermination();
        } catch (InterruptedException | IOException e) {
            System.err.println( "Node "+node.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void findSuccessor(NodeId request, StreamObserver<Node> responseObserver) {
        responseObserver.onNext(GrpcUtil.toNode(node.findSuccessor(request.getId())));
        responseObserver.onCompleted();
    }

    @Override
    public void getPredecessor(Empty request, StreamObserver<Node> responseObserver) {
        responseObserver.onNext(GrpcUtil.toNode(node.getPredecessor()));
        responseObserver.onCompleted();
    }

    @Override
    public void getSuccessor(Empty request, StreamObserver<Node> responseObserver) {
        responseObserver.onNext(GrpcUtil.toNode(node.getSuccessor()));
        responseObserver.onCompleted();
    }

    @Override
    public void notify(Node request, StreamObserver<Empty> responseObserver) {
        node.notify(GrpcUtil.fromNode(request));

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void put(Entry request, StreamObserver<PutResponse> responseObserver) {
        responseObserver.onNext(node.put(request.getKey(), request.getValue()));
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        responseObserver.onNext(node.get(request.getKey()));
        responseObserver.onCompleted();
    }

    public void shutdownNow() {
        server.shutdownNow();
    }

    public ChordNode getNode() {
        return node;
    }
}