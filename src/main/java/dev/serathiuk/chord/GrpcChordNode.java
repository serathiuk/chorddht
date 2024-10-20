package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.*;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcChordNode implements ChordNode, Closeable {

    private final String id;
    private final String host;
    private final int port;
    private final ManagedChannel channel;

    public GrpcChordNode(String host, int port) {
        this.host = host;
        this.port = port;
        this.id = Key.hash(host, port);

        channel = Grpc.newChannelBuilder(host+":"+port, InsecureChannelCredentials.create()).build();
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
        return GrpcUtil.fromNode(createStub().getSuccessor(Empty.getDefaultInstance()));
    }

    private ChordGrpc.ChordBlockingStub createStub() {
        return ChordGrpc.newBlockingStub(channel).withDeadlineAfter(60, TimeUnit.SECONDS);
    }

    @Override
    public ChordNode getPredecessor() {
        return GrpcUtil.fromNode(createStub().getPredecessor(Empty.getDefaultInstance()));
    }

    @Override
    public void notify(ChordNode node) {
        createStub().notify(GrpcUtil.toNode(node));
    }

    @Override
    public ChordNode findSuccessor(String id) {
        return GrpcUtil.fromNode(createStub().findSuccessor(NodeId.newBuilder().setId(id).build()));
    }

    @Override
    public ChordNode closestPrecedingNode(String id) {
        return GrpcUtil.fromNode(createStub().closestPrecedingNode(NodeId.newBuilder().setId(id).build()));
    }

    @Override
    public PutResponse put(String key, String value) {
        return createStub().put(Entry.newBuilder()
                .setKey(key)
                .setValue(value)
                .build());
    }

    @Override
    public GetResponse get(String key) {
        return createStub().get(GetRequest.newBuilder()
                .setKey(key)
                .build());
    }

    @Override
    public void close() throws IOException {
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
