package dev.serathiuk.chord;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChordUtilTest {

    private static final long NODE_ID = 3;
    private static final long NUMBER_OF_BITS_KEY = 5;

    @Test
    public void testPosition1() {
        assertEquals(4, ChordUtil.calculateFTNode(NODE_ID, 1, NUMBER_OF_BITS_KEY));
    }

    @Test
    public void testPosition2() {
        assertEquals(5, ChordUtil.calculateFTNode(NODE_ID, 2, NUMBER_OF_BITS_KEY));
    }

    @Test
    public void testPosition3() {
        assertEquals(7, ChordUtil.calculateFTNode(NODE_ID, 3, NUMBER_OF_BITS_KEY));
    }

    @Test
    public void testPosition4() {
        assertEquals(11, ChordUtil.calculateFTNode(NODE_ID, 4, NUMBER_OF_BITS_KEY));
    }

    @Test
    public void testPosition5() {
        assertEquals(19, ChordUtil.calculateFTNode(NODE_ID, 4, NUMBER_OF_BITS_KEY));
    }

}