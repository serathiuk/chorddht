package dev.serathiuk.chord;

import java.math.BigInteger;

public class ChordUtil {

    public static String calculateFTNode(String nodeId, int ftPosition, int numberOfBitsOfKey) {
        return new BigInteger(nodeId)
                .add(BigInteger.valueOf(2).pow(ftPosition - 1))
                .mod(BigInteger.valueOf(2).pow(numberOfBitsOfKey))
                .toString();
    }

}
