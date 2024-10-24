package dev.serathiuk.chord.server;

import dev.serathiuk.chord.server.grpc.*;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcChordNode implements ChordNode, Closeable {

    private final Logger logger = LoggerFactory.getLogger(LocalChordNode.class);

    private final String id;
    private final String host;
    private final int port;
    private final ManagedChannel channel;

    public GrpcChordNode(String host, int port) {
        this.host = host;
        this.port = port;
        this.id = Key.hash(host, port);

        channel = Grpc.newChannelBuilder(host+":"+port, InsecureChannelCredentials.create())
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
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

    private ChordGrpc.ChordBlockingStub createStub() {
        return ChordGrpc.newBlockingStub(channel).withDeadlineAfter(60, TimeUnit.SECONDS);
    }

    @Override
    public ChordNode getSuccessor() {
        try {
            return GrpcUtil.fromNode(createStub().getSuccessor(Empty.getDefaultInstance()));
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Successor", e);
            return null;
        }
    }

    @Override
    public ChordNode getPredecessor() {
        try {
            return GrpcUtil.fromNode(createStub().getPredecessor(Empty.getDefaultInstance()));
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return null;
        }
    }

    @Override
    public boolean notify(ChordNode node) {
        try {
            createStub().notify(GrpcUtil.toNode(node));
            return true;
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return false;
        }
    }

    @Override
    public ChordNode findSuccessor(String id) {
        try {
            return GrpcUtil.fromNode(createStub().findSuccessor(NodeId.newBuilder().setId(id).build()));
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return null;
        }
    }

    @Override
    public ChordNode closestPrecedingNode(String id) {
        try {
            return GrpcUtil.fromNode(createStub().closestPrecedingNode(NodeId.newBuilder().setId(id).build()));
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return null;
        }
    }

    @Override
    public PutResponse put(String key, String value) {
        try {
            return createStub().put(Entry.newBuilder()
                    .setKey(key)
                    .setValue(value)
                    .build());
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return null;
        }
    }

    @Override
    public GetResponse get(String key) {
        try {
            return createStub().get(GetRequest.newBuilder()
                    .setKey(key)
                    .build());
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return null;
        }
    }

    @Override
    public boolean isOnline() {
        try {
            createStub().ping(Empty.newBuilder().build());
            return true;
        } catch (StatusRuntimeException e) {
            logger.error("Error searching for Predecessor", e);
            return false;
        }
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
