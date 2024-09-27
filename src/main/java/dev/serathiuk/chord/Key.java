package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.Node;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key {

    public static final int NUMBER_OF_BITS_KEY = 120;
    public static final BigInteger SIZE_OF_RING = BigInteger.valueOf(2).pow(NUMBER_OF_BITS_KEY);

    public static String hash(String key) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            var hashBytes = md.digest(key.getBytes());
            var hash = new BigInteger(1, hashBytes);
            return hash.mod(SIZE_OF_RING).add(BigInteger.valueOf(1)).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hash(String host, int port) {
        var key = host + ":" + port;
        return hash(key);
    }

    public static boolean isBetween(BigInteger n, BigInteger start, BigInteger end) {
        if(start.compareTo(end) > 0) {
            return  isBetween(n, start, SIZE_OF_RING.subtract(BigInteger.valueOf(1))) ||
                    isBetween(n, BigInteger.valueOf(0), end);
        }

        return n.compareTo(start) >= 0 && n.compareTo(end) < 0;
    }

    public static boolean isBetween(String n, String start, String end) {
        if(n == null || start == null || end == null) return false;
        return isBetween(new BigInteger(n), new BigInteger(start), new BigInteger(end));
    }

    public static boolean isBetween(Node n, Node start, Node end) {
        if(n == null || start == null || end == null) return false;
        return isBetween(n.getId(), start.getId(), end.getId());
    }

}
