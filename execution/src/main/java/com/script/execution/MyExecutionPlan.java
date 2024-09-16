package com.script.execution;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 *   I am performing a topological sort to ensure that each script is executed after all its dependencies have been executed.
 *   Each `VulnerabilityScript` object can be treated as a node in the graph, and its dependencies can be
 *   treated as directed edges from those dependent scripts to the current script.
 *   Steps
 *    - Building the adjacency list to represent the graph.
 *    - Using Kahn's Algorithm (BFS) for topological sorting by keeping track of in-degrees (number of incoming edges) of each node.
 *    - Adding nodes with 0 in-degree to the queue, and then reducing the in-degrees of their neighbors when they're processed.
 *    - Repeating until all nodes are processed.
 */
@Component
public class MyExecutionPlan {
    public List<Integer> generateExecutionPlan(List<dsa.program.VulnerabilityScript> scripts) {
        Map<Integer, Integer> inDegree = new HashMap<>(); // represent indegree
        Map<Integer, List<Integer>> adjList = new HashMap<>(); // represent dependencies

        for (dsa.program.VulnerabilityScript script : scripts) {
            inDegree.put(script.scriptId(), 0);
            adjList.put(script.scriptId(), new ArrayList<>());
        }

        for (dsa.program.VulnerabilityScript script : scripts) {
            int scriptId = script.scriptId();
            for (int dep : script.dependencies()) {
                adjList.get(dep).add(scriptId);
                inDegree.put(scriptId, inDegree.get(scriptId) + 1);
            }
        }

        Queue<Integer> queue = new LinkedList<>(); //queue for processing zero indegree

        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey()); // adding script with zero indegree / no dependency
            }
        }

        List<Integer> executionPlan = new ArrayList<>();

        while (!queue.isEmpty()) {
            int currentScript = queue.poll();
            executionPlan.add(currentScript);

            // Reduce the in-degree of dependent scripts
            for (int neighbor : adjList.get(currentScript)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // If in-degree becomes 0, add it to the queue
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Check if all scripts have been processed
        if (executionPlan.size() != scripts.size()) {
            throw new IllegalArgumentException("Circular dependency detected or invalid input provided");
        }
        return executionPlan;
    }
}