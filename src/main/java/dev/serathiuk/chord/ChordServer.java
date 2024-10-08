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
    private ExecutorService executorService;

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
                    .keepAliveTime(5, TimeUnit.SECONDS)  // Tempo entre verificações de keepalive
                    .keepAliveTimeout(10, TimeUnit.SECONDS)
                    .build()
                    .start();

            System.out.println("Server started, listening on " + node.getHost()+":"+node.getPort());

            // Shutdown hook para fechar o servidor corretamente
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("** Servidor encerrado via shutdown hook **");
                server.shutdownNow();
            }));

            started = true;

            executorService = Executors.newFixedThreadPool(1);
            executorService.execute(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(!StringUtils.isEmpty(config.remoteHost()) && config.remotePort() > 0) {
                    node.join(new GrpcChordNode(config.remoteHost(), config.remotePort()));
                }

                while(started) {
                    try {
                        Thread.sleep(500);
                        node.fixFingers();
                        node.stabilize();
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
    public void getNodeData(Empty request, StreamObserver<NodeData> responseObserver) {
        responseObserver.onNext(NodeData.newBuilder()
                .setNode(GrpcUtil.toNode(node))
                .setNodeSuccessor(GrpcUtil.toNode(node.getSuccessor()))
                .setNodePredecessor(GrpcUtil.toNode(node.getPredecessor()))
                .build());

        responseObserver.onCompleted();
    }

    @Override
    public void notify(Node request, StreamObserver<Empty> responseObserver) {
        node.notify(GrpcUtil.fromNode(request));
    }

    @Override
    public void put(Entry request, StreamObserver<PutResponse> responseObserver) {
        super.put(request, responseObserver);
    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        super.get(request, responseObserver);
    }

    public void shutdownNow() {
        server.shutdownNow();
    }

    public ChordNode getNode() {
        return node;
    }
}