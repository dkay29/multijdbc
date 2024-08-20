package org.dkay229.multijdbc;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CircularBufferTest {

    private CircularBuffer<String> buffer;

    @BeforeEach
    public void setUp() {
        buffer = new CircularBuffer<>(3);  // Initialize buffer with capacity of 3
    }

    @Test
    public void testAddAndGet() {
        buffer.add("A");
        buffer.add("B");
        buffer.add("C");

        assertEquals("A", buffer.get(), "First element should be 'A'");
        assertEquals("B", buffer.get(), "Second element should be 'B'");
        assertEquals("C", buffer.get(), "Third element should be 'C'");
        assertNull(buffer.get(), "Buffer should be empty now, so get() should return null");
    }

    @Test
    public void testOverwrite() {
        buffer.add("A");
        buffer.add("B");
        buffer.add("C");

        // Buffer is full now, next add should overwrite the oldest element
        buffer.add("D");

        assertEquals("B", buffer.get(), "First element should be 'B' after overwrite");
        assertEquals("C", buffer.get(), "Second element should be 'C'");
        assertEquals("D", buffer.get(), "Third element should be 'D'");
        assertNull(buffer.get(), "Buffer should be empty now, so get() should return null");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(buffer.isEmpty(), "Buffer should be empty initially");

        buffer.add("A");
        assertFalse(buffer.isEmpty(), "Buffer should not be empty after adding an element");

        buffer.get();
        assertTrue(buffer.isEmpty(), "Buffer should be empty after removing the only element");
    }

    @Test
    public void testIsFull() {
        assertFalse(buffer.isFull(), "Buffer should not be full initially");

        buffer.add("A");
        buffer.add("B");
        buffer.add("C");

        assertTrue(buffer.isFull(), "Buffer should be full after adding 3 elements");

        buffer.get();
        assertFalse(buffer.isFull(), "Buffer should not be full after removing an element");
    }

    @Test
    public void testSize() {
        assertEquals(0, buffer.size(), "Buffer size should be 0 initially");

        buffer.add("A");
        assertEquals(1, buffer.size(), "Buffer size should be 1 after adding one element");

        buffer.add("B");
        buffer.add("C");
        assertEquals(3, buffer.size(), "Buffer size should be 3 after adding three elements");

        buffer.get();
        assertEquals(2, buffer.size(), "Buffer size should be 2 after removing one element");
    }

    @Test
    public void testFreeSpace() {
        assertEquals(3, buffer.freeSpace(), "Free space should be equal to buffer size initially");

        buffer.add("A");
        assertEquals(2, buffer.freeSpace(), "Free space should be 2 after adding one element");

        buffer.add("B");
        buffer.add("C");
        assertEquals(0, buffer.freeSpace(), "Free space should be 0 when buffer is full");

        buffer.get();
        assertEquals(1, buffer.freeSpace(), "Free space should be 1 after removing one element");
    }

    @Test
    public void testOverwriteAndFreeSpace() {
        buffer.add("A");
        buffer.add("B");
        buffer.add("C");

        buffer.add("D");  // Overwrite the oldest element

        assertEquals(0, buffer.freeSpace(), "Free space should be 0 after adding elements to full capacity");
        assertEquals(3, buffer.size(), "Buffer size should still be 3 after overwriting");
    }
}
