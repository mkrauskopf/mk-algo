/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Martin Krauskopf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mk.tsp;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;

import java.util.Stack;

import static com.google.common.base.Preconditions.checkState;

/**
 * Implementation of TSP problem using naive top-down recursive algorithm with time complexity O(n!).
 */
public final class NaiveTsp implements TspSolver {

    /** Let's utilise Guava's graph capabilities instead of the hand-made adjacency matrix. */
    private final ValueGraph<Integer, Integer> graph;

    /** Helper field keeping number of graph vertices (nodes). */
    private final int nOfVertices;

    private final int startingVertex;

    /** Keeps the best cost during iteration. */
    private int bestCost;

    /** Keeps the best past during iteration. */
    private ImmutableList<Integer> bestPath;

    public NaiveTsp(ImmutableValueGraph<Integer, Integer> graph, int startingVertex) {
        this.graph = graph;
        this.bestCost = Integer.MAX_VALUE;
        this.nOfVertices = graph.nodes().size();
        checkState(startingVertex < nOfVertices, "starting point exists");
        this.startingVertex = startingVertex;
    }

    @Override
    public TspPath findBestPath() {
        Stack<Integer> currentPath = new Stack<>();
        currentPath.push(startingVertex);
        findBestPath(startingVertex, 0, currentPath);
        return new TspPath(bestPath, bestCost);
    }

    private void findBestPath(int root, int pathCost, Stack<Integer> currentPath) {
        if (currentPath.size() == nOfVertices) { // All vertices visited. Back to the starting vertex.
            Integer backToHomeValue = graph.edgeValue(root, startingVertex).get();
            int finalCost = pathCost + backToHomeValue;
            currentPath.push(startingVertex);
            if (finalCost < bestCost) {
                bestCost = finalCost;
                bestPath = ImmutableList.copyOf(currentPath);
                System.out.printf("\nBetter solution: cost = %d, path = %s\n", bestCost, bestPath);
            }
            currentPath.pop();
        }

        for (int nextToVisit = 0; nextToVisit < nOfVertices; nextToVisit++) {
            if (currentPath.contains(nextToVisit)) { // already visited
                continue;
            }
            Integer edgeValue = graph.edgeValue(root, nextToVisit).get();
            int newCost = pathCost + edgeValue;
            currentPath.push(nextToVisit);
            findBestPath(nextToVisit, newCost, currentPath);
            currentPath.pop();
        }
    }

}
