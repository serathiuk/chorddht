package dev.serathiuk.chord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    public void testHash() {
        assertEquals("246", Key.hash("teste"));
    }

    @Test
    public void testHashNode() {
        assertEquals("169", Key.hash("localhost", 8080));
    }

    @Test
    public void testIsBetween() {
        assertTrue(Key.isBetween("2", "1", "3", true));
        assertFalse(Key.isBetween("2", "3", "7", true));
        assertTrue(Key.isBetween("2", "27", "5",true));
        assertFalse(Key.isBetween("25", "27", "5",true));
        assertTrue(Key.isBetween("3", "3", "3", true));

        assertFalse(Key.isBetween("3", "1", "3", false));
        assertTrue(Key.isBetween("2", "1", "3", false));
        assertFalse(Key.isBetween("5", "27", "5",false));
        assertTrue(Key.isBetween("4", "27", "5",false));
        assertFalse(Key.isBetween("3", "3", "3", false));
    }

}