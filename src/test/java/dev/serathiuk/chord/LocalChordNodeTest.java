package dev.serathiuk.chord;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class LocalChordNodeTest {

    private Logger logger = LoggerFactory.getLogger(LocalChordNodeTest.class);

    @Test
    public void testNodes() throws InterruptedException {


        LocalChordNode node1 = new LocalChordNode("localhost", 8080); // 169
        assertEquals(node1.getId(), node1.getSuccessor().getId());

        LocalChordNode node2 = new LocalChordNode("localhost", 8081); // 232
        node2.join(node1);

        node1.stabilize();
        node2.stabilize();

        assertEquals(node1.getId(), node2.getSuccessor().getId());
        assertEquals(node2.getId(), node1.getSuccessor().getId());

        System.out.println(" node1: "+node1.getPredecessor().getId()+" -> "+node1.getId()+" -> "+node1.getSuccessor().getId());
        System.out.println(" node2: "+node2.getPredecessor().getId()+" -> "+node2.getId()+" -> "+node2.getSuccessor().getId());

        LocalChordNode node3 = new LocalChordNode("localhost", 8082);
        node3.join(node1);

        for(var i = 0; i < 10; i++) {
            node1.fixFingers();
            node2.fixFingers();
            node3.fixFingers();
        }

        LocalChordNode node4 = new LocalChordNode("localhost", 8083);
        node4.join(node1);

        for(var i = 0; i < 10; i++) {
            node1.fixFingers();
            node2.fixFingers();
            node3.fixFingers();
            node4.fixFingers();
        }


        LocalChordNode node5 = new LocalChordNode("localhost", 8084);
        node5.join(node1);

        for(var i = 0; i < 10; i++) {
            node1.fixFingers();
            node2.fixFingers();
            node3.fixFingers();
            node4.fixFingers();
            node5.fixFingers();
        }


//        8080 => 169 -> node1
//        8081 => 43 -> node2
//        8082 => 232 -> node3
//        8083 => 131 -> node4
//        8084 => 35 -> node5
//        node5 - node2 - node4 - node1 - node3 -> node5

        System.out.println(" node1: "+node1.getPredecessor().getId()+" -> "+node1.getId()+" -> "+node1.getSuccessor().getId());
        System.out.println(" node2: "+node2.getPredecessor().getId()+" -> "+node2.getId()+" -> "+node2.getSuccessor().getId());
        System.out.println(" node3: "+(node3.getPredecessor() != null ? node3.getPredecessor().getId() : null)+" -> "+node3.getId()+" -> "+node3.getSuccessor().getId());
        System.out.println(" node4: "+node4.getPredecessor().getId()+" -> "+node4.getId()+" -> "+node4.getSuccessor().getId());
        System.out.println(" node5: "+node5.getPredecessor().getId()+" -> "+node5.getId()+" -> "+node5.getSuccessor().getId());

        assertEquals(node3.getId(), node1.getSuccessor().getId());
        assertEquals(node4.getId(), node2.getSuccessor().getId());
        assertEquals(node5.getId(), node3.getSuccessor().getId());
        assertEquals(node1.getId(), node4.getSuccessor().getId());
        assertEquals(node2.getId(), node5.getSuccessor().getId());

//        LocalChordNode node3 = new LocalChordNode("localhost", 8082);
////        LocalChordNode node4 = new LocalChordNode("localhost", 8083);
////        LocalChordNode node5 = new LocalChordNode("localhost", 8084);
//
//        node2.join(node1);
//        node3.join(node1);
////        node4.join(node1);
////        node5.join(node1);
//
////        node4 - node5 - node3 - node1 - node2 -> node5
//
////        assertEquals(node5.getId(), node2.getSuccessor().getId());
//        assertEquals(node3.getId(), node2.getSuccessor().getId());
////        assertEquals(node5.getId(), node4.getSuccessor().getId());
//        assertEquals(node1.getId(), node3.getSuccessor().getId());
    }

    public static void main(String[] args) {
        System.out.println("8080 => "+Key.hash("localhost", 8080));
        System.out.println("8081 => "+Key.hash("localhost", 8081));
        System.out.println("8082 => "+Key.hash("localhost", 8082));
        System.out.println("8083 => "+Key.hash("localhost", 8083));
        System.out.println("8084 => "+Key.hash("localhost", 8084));

    }

}