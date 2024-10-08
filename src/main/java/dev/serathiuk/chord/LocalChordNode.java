package dev.serathiuk.chord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalChordNode implements ChordNode, Joinable {

    private Logger logger = LoggerFactory.getLogger(LocalChordNode.class);

    private String id;
    private String host;
    private int port;
    private ChordNode successor;
    private ChordNode predecessor;
    private FingerTable fingerTable;
    private Lock lock = new ReentrantLock();

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
    public void notify(ChordNode node) {
        try {
            lock.lock();

            logger.info("Node {} notified by node: {}", id, node.getId());

            if (predecessor == null || Key.isBetween(node.getId(), predecessor.getId(), id, false)) {
                logger.info("Node {} new predecessor: {}", id, node.getId());
                predecessor = node;
            }

            if (successor == null || Key.isBetween(node.getId(), id, successor.getId(), false)) {
                logger.info("Node {} new successor: {}", id, node.getId());
                successor = node;
                fingerTable.setSuccessor(successor);
            }
        } finally {
            lock.unlock();
        }
    }

    public void stabilize() {
        try {
            lock.lock();

            logger.info("Node {} start stabilization.", id);

            if(successor == null)
                return;

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

}
