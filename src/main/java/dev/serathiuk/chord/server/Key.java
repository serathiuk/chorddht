package dev.serathiuk.chord.server;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key {

    private Key() {}

    public static String hash(String key) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            var hashBytes = md.digest(key.getBytes());
            var hash = new BigInteger(1, hashBytes);
            return hash.mod(ChordNode.SIZE_OF_RING).add(BigInteger.valueOf(1)).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hash(String host, int port) {
        var key = host + ":" + port;
        return hash(key);
    }

    public static boolean isBetween(BigInteger n, BigInteger start, BigInteger end, boolean inclusiveEnd) {
        if(start.compareTo(end) >= 0) {
            return  isBetween(n, start, ChordNode.SIZE_OF_RING.subtract(BigInteger.valueOf(1)), true) ||
                    isBetween(n, BigInteger.valueOf(0), end, inclusiveEnd);
        }

        return n.compareTo(start) > 0 && (inclusiveEnd ? n.compareTo(end) <= 0 : n.compareTo(end) < 0);
    }

    public static boolean isBetween(String n, String start, String end, boolean inclusiveEnd) {
        if(StringUtils.isEmpty(n) || StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) return false;
        return isBetween(new BigInteger(n), new BigInteger(start), new BigInteger(end), inclusiveEnd);
    }

}
