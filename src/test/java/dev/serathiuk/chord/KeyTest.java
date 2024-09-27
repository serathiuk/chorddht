package dev.serathiuk.chord;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    public void testHash() {
        assertEquals(new BigInteger("1179346398922162176738258790724679158"), Key.hash("teste"));
    }

    @Test
    public void testHashNode() {
        assertEquals(new BigInteger("365804962626599491947414221087571625"), Key.hash("localhost", 8080));
    }

    @Test
    public void testHashNode2() {

    }

}