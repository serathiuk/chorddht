package dev.serathiuk.chord.server;

import dev.serathiuk.chord.server.grpc.GetResponse;
import dev.serathiuk.chord.server.grpc.PutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalChordNode implements ChordNode, Joinable {

    private final Logger logger = LoggerFactory.getLogger(LocalChordNode.class);

    private final String id;
    private final String host;
    private final int port;
    private ChordNode successor;
    private ChordNode predecessor;
    private final FingerTable fingerTable;
    private final Lock lock = new ReentrantLock();
    private final Map<String, String> mapNodeData = Collections.synchronizedMap(new HashMap<>());
    private boolean online = true;


    public LocalChordNode(String host, int port) {
        this.id = Key.hash(host, port);
        this.host = host;
        this.port = port;
        this.fingerTable = new FingerTable(this);
        this.successor = this;
        this.predecessor = this;
    }

    @Override
    public ChordNode getSuccessor() {
        return successor;
    }

    @Override
    public ChordNode getPredecessor() {
        return predecessor;
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
    public ChordNode findSuccessor(String id) {
        if(this.id.equals(id))
            return this;

        logger.info("Finding successor for id: {}", id);
        var x = findPredecessor(id).getSuccessor();

        if (x != null) {
            logger.info("Successor found: {}", x.getId());
        }

        return x != null ? x : this;
    }

    private ChordNode findPredecessor(String id) {
        ChordNode n = this;

        if(n.getSuccessor() == null) {
            return n;
        }

        while(!Key.isBetween(id, n.getId(), n.getSuccessor().getId(), true)) {
            var x = n.closestPrecedingNode(id);
            if(x.getId().equals(this.id)) {
                break;
            } else if(x.getId().equals(n.getId())) {
                break;
            }

            n = x;
        }

        return n;
    }

    @Override
    public ChordNode closestPrecedingNode(String id) {
        try {
            lock.lock();
            return fingerTable.closestPrecedingNode(id);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void join(ChordNode node) {
        try {
            lock.lock();

            logger.info("Node {} joining node: {}", id, node.getId());

            ChordNode x  = node.findSuccessor(id);
            predecessor = x != null ? x.getPredecessor() : null;
            successor = x != null ? x : node;
            fingerTable.setSuccessor(successor);

            logger.info("Node {} sucessor: {}", id, successor.getId());

            successor.notify(this);
            predecessor.notify(this);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean notify(ChordNode node) {
        try {
            lock.lock();

            logger.info("Node {} notified by node: {}", id, node.getId());

            if (predecessor == null || !predecessor.isOnline() || Key.isBetween(node.getId(), predecessor.getId(), id, false)) {
                logger.info("Node {} new predecessor: {}", id, node.getId());
                predecessor = node;
            }

            if (successor == null || !successor.isOnline() || Key.isBetween(node.getId(), id, successor.getId(), false)) {
                logger.info("Node {} new successor: {}", id, node.getId());
                successor = node;
                fingerTable.setSuccessor(successor);
            }

            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public PutResponse put(String key, String value) {
        var hash = Key.hash(key);
        logger.info("Node {} put key: {} hash: {}", id, key, hash);
        if(Key.isBetween(hash, id, successor.getId(), false)) {
            logger.info("Puting the value in Node {} put key: {} value: {}", id, key, value);
            mapNodeData.put(key, value);
            return PutResponse.newBuilder()
                    .setNodeId(id)
                    .setKey(key)
                    .setValue(value)
                    .build();
        }

        return getNextNode(hash).put(key, value);
    }

    @Override
    public GetResponse get(String key) {
        var hash = Key.hash(key);
        if(Key.isBetween(hash, id, successor.getId(), true)) {
            var node =  mapNodeData.get(key);
            return GetResponse.newBuilder()
                    .setValue(node)
                    .setNodeId(id)
                    .setKey(key)
                    .build();
        }

        return getNextNode(hash).get(key);
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    private ChordNode getNextNode(String hash) {
        ChordNode node = this;
        if(!Key.isBetween(hash, node.getId(), node.getSuccessor().getId(), false)) {
            node = node.findSuccessor(hash);
        }
        return node.getPredecessor();
    }

    public void stabilize() {
        try {
            lock.lock();

            logger.info("Node {} start stabilization.", id);

            if(successor == null)
                return;

            if(!successor.isOnline()) {
                logger.info("Node {} stabilization successor is offline.", id);
                fingerTable.setSuccessor(null);
                successor = fingerTable.findSuccessor()
                        .orElse(this);
            }

            var x = successor.getPredecessor();

            if(x != null) {
                logger.info("Node {} stabilization possible successor: {}", id, x.getId());
            } else {
                logger.info("Node {} stabilization possible successor: null", id);
            }

            if (x != null && Key.isBetween(x.getId(), id, successor.getId(), false)) {
                logger.info("Node {} stabilization changed the successor: {}", id, x.getId());
                this.successor = x;
                fingerTable.setSuccessor(x);
                successor.notify(this);
            }
        } finally {
            lock.unlock();
        }
    }

    public void fixFingers() {
        try {
            lock.lock();

            logger.info("Node {} fix fingers.", id);

            fingerTable.setSuccessor(successor);
            fingerTable.fixFingers();
        } finally {
            lock.unlock();
        }
    }

    public void shutdownNow() {
        if(!successor.getId().equals(id) && successor.isOnline()) {
            successor.notify(predecessor);
        }

        if(!predecessor.getId().equals(id) && predecessor.isOnline()) {
            predecessor.notify(successor);
        }
    }

}
