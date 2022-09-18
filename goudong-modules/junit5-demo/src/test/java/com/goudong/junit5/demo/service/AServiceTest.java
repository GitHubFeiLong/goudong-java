package com.goudong.junit5.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AServiceTest {

    private AService aServiceUnderTest;

    @BeforeEach
    void setUp() {
        aServiceUnderTest = new AService();
        aServiceUnderTest.bService = mock(BService.class);
    }

    @Test
    void testAdd() {
        assertEquals(0, aServiceUnderTest.add(0, 0));
    }

    @Test
    void testBadd() {
        // Setup
        when(aServiceUnderTest.bService.badd(0, 0)).thenReturn(0);

        // Run the test
        final int result = aServiceUnderTest.badd(0, 0);

        // Verify the results
        assertEquals(0, result);
    }

    @Test
    void testSub() {
        assertEquals(0, aServiceUnderTest.sub(0, 0));
    }
}
