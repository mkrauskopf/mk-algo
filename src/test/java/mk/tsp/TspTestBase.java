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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

abstract class TspTestBase {

    protected abstract TspSolver createTspSolver(ImmutableValueGraph<Integer, Integer> generateGraph, int i);

    @Test
    public void findBestPathTrivialGraph() {
        int[][] graph3 = {
                {0, 1, 4},
                {1, 0, 5},
                {4, 5, 0},
        };
        TspSolver tsp = createTspSolver(TspTests.generateGraph(graph3), 0);
        TspPath path = tsp.findBestPath();
        TspPath expectedPath = new TspPath(ImmutableList.of(0, 1, 2, 0), 10);
        assertEquals("expected path", expectedPath, path);
    }

    @Test
    public void findBestPathFromVertex0() {
        TspSolver tsp = createTspSolver(TspTests.generateRandomGraph(4, 42), 0);
        TspPath path = tsp.findBestPath();
        TspPath expectedPath = new TspPath(ImmutableList.of(0, 1, 3, 2, 0), 12);
        assertEquals("expected path", expectedPath, path);
    }

    @Test
    public void findBestPathFromLastVertex() {
        TspSolver tsp = createTspSolver(TspTests.generateRandomGraph(4, 42), 3);
        TspPath path = tsp.findBestPath();
        TspPath expectedPath = new TspPath(ImmutableList.of(3, 1, 0, 2, 3), 12);
        assertEquals("expected path", expectedPath, path);
    }

    @Test
    public void findBestPathFromLastVertexLarge() {
        TspSolver tsp = createTspSolver(TspTests.generateRandomGraph(11, 42), 3);
        TspPath path = tsp.findBestPath();
        TspPath expectedPath = new TspPath(ImmutableList.of(3, 6, 7, 10, 2, 4, 8, 5, 0, 1, 9, 3), 27);
        assertEquals("expected path", expectedPath, path);
    }

    @Test(expected = IllegalStateException.class)
    public void startingPointExist() {
        createTspSolver(TspTests.generateRandomGraph(4, 42), 4);
    }

}
