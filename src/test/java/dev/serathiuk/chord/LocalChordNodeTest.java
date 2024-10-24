package dev.serathiuk.chord;

import dev.serathiuk.chord.server.Key;
import dev.serathiuk.chord.server.LocalChordNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalChordNodeTest {

    @Test
    public void testNodes() {


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
//        node5 (35-42) - node2 (43-130) - node4 (131-168) - node1 (169-231) - node3 (231-34) -> node5 (35-42)

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

//        node5 (35-42) - node2 (43-130) - node4 (131-168) - node1 (169-231) - node3 (231-34) -> node5 (35-42)
//        teste1 => 31 // no 3
//        teste2 => 251 //no 3
//        teste3 => 187 // no 1
//        teste4 => 159 // no 4
//        teste5 => 124 // no 2
//        teste6 => 73 // no 2
//        teste7 => 167 // no 4
//        teste8 => 248 // no 3
//        teste9 => 52 //no 2
//        teste10 => 137 // no 4

        node1.put("teste1", "valor1");
        node1.put("teste2", "valor2");
        node2.put("teste3", "valor3");
        node2.put("teste4", "valor4");
        node3.put("teste5", "valor5");
        node3.put("teste6", "valor6");
        node4.put("teste7", "valor7");
        node4.put("teste8", "valor8");
        node5.put("teste9", "valor9");
        node5.put("teste10", "valor10");

        var teste1 = node5.get("teste1");
        var teste2 = node4.get("teste2");
        var teste3 = node3.get("teste3");
        var teste4 = node2.get("teste4");
        var teste5 = node1.get("teste5");
        var teste6 = node5.get("teste6");
        var teste7 = node4.get("teste7");
        var teste8 = node3.get("teste8");
        var teste9 = node2.get("teste9");
        var teste10 = node1.get("teste10");

        assertEquals("teste1", teste1.getKey());
        assertEquals("valor1", teste1.getValue());
        assertEquals(node3.getId(), teste1.getNodeId());

        assertEquals("teste2", teste2.getKey());
        assertEquals("valor2", teste2.getValue());
        assertEquals(node3.getId(), teste2.getNodeId());

        assertEquals("teste3", teste3.getKey());
        assertEquals("valor3", teste3.getValue());
        assertEquals(node1.getId(), teste3.getNodeId());

        assertEquals("teste4", teste4.getKey());
        assertEquals("valor4", teste4.getValue());
        assertEquals(node4.getId(), teste4.getNodeId());

        assertEquals("teste5", teste5.getKey());
        assertEquals("valor5", teste5.getValue());
        assertEquals(node2.getId(), teste5.getNodeId());

        assertEquals("teste6", teste6.getKey());
        assertEquals("valor6", teste6.getValue());
        assertEquals(node2.getId(), teste6.getNodeId());

        assertEquals("teste7", teste7.getKey());
        assertEquals("valor7", teste7.getValue());
        assertEquals(node4.getId(), teste7.getNodeId());

        assertEquals("teste8", teste8.getKey());
        assertEquals("valor8", teste8.getValue());
        assertEquals(node3.getId(), teste8.getNodeId());

        assertEquals("teste9", teste9.getKey());
        assertEquals("valor9", teste9.getValue());
        assertEquals(node2.getId(), teste9.getNodeId());

        assertEquals("teste10", teste10.getKey());
        assertEquals("valor10", teste10.getValue());
        assertEquals(node4.getId(), teste10.getNodeId());

        node3.setOnline(false);
        node3.shutdownNow();

        for(var i = 0; i < 10; i++) {
            node1.fixFingers();
            node1.stabilize();
            node2.fixFingers();
            node2.stabilize();
            node4.fixFingers();
            node4.stabilize();
            node5.fixFingers();
            node5.stabilize();
        }

        assertEquals(node5.getId(), node1.getSuccessor().getId());
        assertEquals(node4.getId(), node2.getSuccessor().getId());
        assertEquals(node1.getId(), node4.getSuccessor().getId());
        assertEquals(node2.getId(), node5.getSuccessor().getId());

        node4.setOnline(false);
        node4.shutdownNow();

        for(var i = 0; i < 10; i++) {
            node1.fixFingers();
            node1.stabilize();
            node2.fixFingers();
            node2.stabilize();
            node5.fixFingers();
            node5.stabilize();
        }

        assertEquals(node5.getId(), node1.getSuccessor().getId());
        assertEquals(node1.getId(), node2.getSuccessor().getId());
        assertEquals(node2.getId(), node5.getSuccessor().getId());

        node2.setOnline(false);
        node2.shutdownNow();

        for(var i = 0; i < 10; i++) {
            node1.fixFingers();
            node1.stabilize();
            node5.fixFingers();
            node5.stabilize();
        }

        assertEquals(node5.getId(), node1.getSuccessor().getId());
        assertEquals(node1.getId(), node5.getSuccessor().getId());

        node1.setOnline(false);
        node1.shutdownNow();

        for(var i = 0; i < 10; i++) {
            node5.fixFingers();
            node5.stabilize();
        }

        assertEquals(node5.getId(), node5.getSuccessor().getId());
    }

    public static void main(String[] args) {
        System.out.println("8080 => "+ Key.hash("localhost", 8080));
        System.out.println("8081 => "+Key.hash("localhost", 8081));
        System.out.println("8082 => "+Key.hash("localhost", 8082));
        System.out.println("8083 => "+Key.hash("localhost", 8083));
        System.out.println("8084 => "+Key.hash("localhost", 8084));

        for (var i = 1; i <= 10; i++) {
            System.out.println("teste"+i+" => "+Key.hash("teste"+i));
        }

    }

}