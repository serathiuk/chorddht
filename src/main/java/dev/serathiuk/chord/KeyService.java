package dev.serathiuk.chord;

import dev.serathiuk.chord.grpc.Node;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyService {

    private int numberOfBits;
    private BigInteger sizeOfRing;

    public KeyService(int numberOfBits) {
        this.numberOfBits = numberOfBits;
        this.sizeOfRing = BigInteger.valueOf(2).pow(numberOfBits);
    }

    public String hash(String key) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            var hashBytes = md.digest(key.getBytes());
            var hash = new BigInteger(1, hashBytes);
            return hash.mod(sizeOfRing).add(BigInteger.valueOf(1)).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String hash(String host, int port) {
        var key = host + ":" + port;
        return hash(key);
    }

    public boolean isBetween(BigInteger n, BigInteger start, BigInteger end, boolean inclusiveEnd) {
        if(start.compareTo(end) >= 0) {
            return  isBetween(n, start, sizeOfRing.subtract(BigInteger.valueOf(1)), true) ||
                    isBetween(n, BigInteger.valueOf(0), end, inclusiveEnd);
        }

        return n.compareTo(start) > 0 && (inclusiveEnd ? n.compareTo(end) <= 0 : n.compareTo(end) < 0);
    }

    public boolean isBetween(String n, String start, String end, boolean inclusiveEnd) {
        if(StringUtils.isEmpty(n) || StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) return false;
        return isBetween(new BigInteger(n), new BigInteger(start), new BigInteger(end), inclusiveEnd);
    }

}
