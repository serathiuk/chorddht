package dev.serathiuk.chord;

import java.math.BigInteger;
import java.util.logging.Logger;

public class ChordUtil {

    private static final Logger logger = Logger.getLogger(ChordUtil.class.getName());

    public static String calculateFTNode(String nodeId, int ftPosition, int numberOfBitsOfKey) {
        try {
            return new BigInteger(nodeId)
                    .add(BigInteger.valueOf(2).pow(ftPosition - 1))
                    .mod(BigInteger.valueOf(2).pow(numberOfBitsOfKey))
                    .toString();
        } catch (ArithmeticException e) {
            logger.severe("Erro ao calcular. nodeId: " + nodeId + ", ftPosition: " + ftPosition + ", numberOfBitsOfKey: " + numberOfBitsOfKey);
            throw new IllegalStateException("Erro ao calcular", e);
        }

    }

}
