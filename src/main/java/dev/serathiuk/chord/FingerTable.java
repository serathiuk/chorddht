package dev.serathiuk.chord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class FingerTable {

    private Logger logger = LoggerFactory.getLogger(FingerTable.class);

    private ChordNode[] fingerTableArr;
    private ChordNode node;

    public FingerTable(ChordNode node) {
        this.node = node;
        this.fingerTableArr = new ChordNode[ChordNode.NUMBER_OF_BITS_KEY];
    }

    public ChordNode closestPrecedingNode(String id) {
        logger.info("Busca closestPrecedingNode para id {}: {}", node.getId(), id);
        for (int i = ChordNode.NUMBER_OF_BITS_KEY - 1; i > 0; i--) {
            var fingerNode = fingerTableArr[i];
            if(fingerNode == null) continue;

            if (Key.isBetween(fingerNode.getId(), node.getId(), id, false)) {
                return fingerNode;
            }
        }
        return node;
    }

    public ChordNode getSuccessor() {
        return fingerTableArr[0];
    }

    public void setSuccessor(ChordNode chordNode) {
        fingerTableArr[0] = chordNode;
    }

    public void fixFingers () {
            var random = new Random();
            var indice = random.nextInt(fingerTableArr.length);
            var identificador = ChordUtil.calculateFTNode(node.getId(), indice+1, fingerTableArr.length);

            if(identificador.equals(node.getId())) {
                return;
            }

            var successor = node.findSuccessor(identificador);
            fingerTableArr[indice] = successor;
    }
}
