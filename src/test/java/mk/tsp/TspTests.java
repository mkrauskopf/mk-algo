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

import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import java.util.Random;

import static java.util.stream.IntStream.range;

/**
 * Helper methods used during testing.
 */
final class TspTests {

    static ImmutableValueGraph<Integer, Integer> generateRandomGraph(int nOfNodes, Integer randomSeed) {
        MutableValueGraph<Integer, Integer> mutableG = ValueGraphBuilder.undirected().build();

        // Generated complete (fully connected) graph with random weights.
        // Vertices are numbered <0, nOfNodes>.
        Random random = randomSeed == null ? new Random() : new Random(randomSeed);
        range(0, nOfNodes).forEach(i -> {
            range(i + 1, nOfNodes).forEach(j -> {
                mutableG.putEdgeValue(i, j, random.nextInt(10) + 1);
            });
        });
        return ImmutableValueGraph.copyOf(mutableG);
    }

    public static ImmutableValueGraph<Integer, Integer> generateGraph(int[][] adjacentMatrix) {
        MutableValueGraph<Integer, Integer> mutableG = ValueGraphBuilder.undirected().build();
        for (int i = 0; i < adjacentMatrix.length; i++) {
            // Only half of the matrix is needed.
            for (int j = i + 1; j < adjacentMatrix[i].length; j++) {
                mutableG.putEdgeValue(i, j, adjacentMatrix[i][j]);
            }
        }
        return ImmutableValueGraph.copyOf(mutableG);
    }

}
