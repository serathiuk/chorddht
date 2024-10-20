package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.GetResponse;
import dev.serathiuk.chord.grpc.PutResponse;

import java.math.BigInteger;

public interface ChordNode {

    public static final int NUMBER_OF_BITS_KEY = 8;
    public static final BigInteger SIZE_OF_RING = BigInteger.valueOf(2).pow(NUMBER_OF_BITS_KEY);

    public String getId();
    public String getHost();
    public int getPort();
    public ChordNode getSuccessor();
    public ChordNode getPredecessor();
    public void notify(ChordNode node);
    public ChordNode findSuccessor(String id);
    public ChordNode closestPrecedingNode(String id);
    public PutResponse put(String key, String value);
    public GetResponse get(String key);
}
