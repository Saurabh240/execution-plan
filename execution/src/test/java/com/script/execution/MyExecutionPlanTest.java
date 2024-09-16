package com.script.execution;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dsa.program.VulnerabilityScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MyExecutionPlanTest {

    @InjectMocks
    private MyExecutionPlan myExecutionPlan;

    @Mock
    private VulnerabilityScript script1;
    @Mock
    private VulnerabilityScript script2;
    @Mock
    private VulnerabilityScript script3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateExecutionPlan_NoDependencies() {
        when(script1.scriptId()).thenReturn(1);
        when(script1.dependencies()).thenReturn(new ArrayList<>());

        when(script2.scriptId()).thenReturn(2);
        when(script2.dependencies()).thenReturn(new ArrayList<>());

        when(script3.scriptId()).thenReturn(3);
        when(script3.dependencies()).thenReturn(new ArrayList<>());

        List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3);

        List<Integer> result = myExecutionPlan.generateExecutionPlan(scripts);

        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }

    @Test
    void testGenerateExecutionPlan_WithDependencies() {
        when(script1.scriptId()).thenReturn(1);
        when(script1.dependencies()).thenReturn(Arrays.asList());

        when(script2.scriptId()).thenReturn(2);
        when(script2.dependencies()).thenReturn(Arrays.asList(1));

        when(script3.scriptId()).thenReturn(3);
        when(script3.dependencies()).thenReturn(Arrays.asList(2));

        List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3);

        List<Integer> result = myExecutionPlan.generateExecutionPlan(scripts);

        assertEquals(3, result.size());
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    // Test for circular dependency edge case
    @Test
    void testGenerateExecutionPlan_CircularDependency() {
        when(script1.scriptId()).thenReturn(1);
        when(script1.dependencies()).thenReturn(Arrays.asList(3));

        when(script2.scriptId()).thenReturn(2);
        when(script2.dependencies()).thenReturn(Arrays.asList(1));

        when(script3.scriptId()).thenReturn(3);
        when(script3.dependencies()).thenReturn(Arrays.asList(2));
        List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> myExecutionPlan.generateExecutionPlan(scripts));

        assertEquals("Circular dependency detected or invalid input provided", exception.getMessage());
    }
}
