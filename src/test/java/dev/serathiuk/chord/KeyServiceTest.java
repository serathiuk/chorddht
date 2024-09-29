package dev.serathiuk.chord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyServiceTest {

    private KeyService keyService;

    @BeforeEach
    public void setUp() {
        keyService = new KeyService(5);
    }

    @Test
    public void testHash() {
        assertEquals("22", keyService.hash("teste"));
    }

    @Test
    public void testHashNode() {
        assertEquals("9", keyService.hash("localhost", 8080));
    }

    @Test
    public void testIsBetween() {
        assertTrue(keyService.isBetween("2", "1", "3", true));
        assertFalse(keyService.isBetween("2", "3", "7", true));
        assertTrue(keyService.isBetween("2", "27", "5",true));
        assertFalse(keyService.isBetween("25", "27", "5",true));
        assertTrue(keyService.isBetween("3", "3", "3", true));

        assertFalse(keyService.isBetween("3", "1", "3", false));
        assertTrue(keyService.isBetween("2", "1", "3", false));
        assertFalse(keyService.isBetween("5", "27", "5",false));
        assertTrue(keyService.isBetween("4", "27", "5",false));
        assertFalse(keyService.isBetween("3", "3", "3", false));
    }

}