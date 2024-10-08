package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.ChordGrpc;
import dev.serathiuk.chord.grpc.Empty;
import dev.serathiuk.chord.grpc.NodeId;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcChordNode implements ChordNode, Closeable {

    private String id;
    private String host;
    private int port;
    private ManagedChannel channel;
    private ChordGrpc.ChordBlockingStub stub;

    public GrpcChordNode(String host, int port) {
        this.host = host;
        this.port = port;
        this.id = Key.hash(host, port);

        channel = Grpc.newChannelBuilder(host+":"+port, InsecureChannelCredentials.create()).build();
        stub = ChordGrpc.newBlockingStub(channel).withDeadlineAfter(5, TimeUnit.SECONDS);

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public ChordNode getSuccessor() {
        return GrpcUtil.fromNode(stub.getNodeData(Empty.getDefaultInstance())
                .getNodeSuccessor());
    }

    @Override
    public ChordNode getPredecessor() {
        return GrpcUtil.fromNode(stub.getNodeData(Empty.getDefaultInstance())
                .getNodePredecessor());
    }

    @Override
    public void notify(ChordNode node) {
        stub.notify(GrpcUtil.toNode(node));
    }

    @Override
    public ChordNode findSuccessor(String id) {
        return GrpcUtil.fromNode(stub.findSuccessor(NodeId.newBuilder().setId(id).build()));
    }

    @Override
    public ChordNode closestPrecedingNode(String id) {
        return GrpcUtil.fromNode(stub.closestPrecedingNode(NodeId.newBuilder().setId(id).build()));
    }

    @Override
    public void close() throws IOException {
        stub = null;

        if (channel != null) {
            channel.shutdown();
            try {
                channel.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                channel.shutdownNow();
            }
        }
    }
}
