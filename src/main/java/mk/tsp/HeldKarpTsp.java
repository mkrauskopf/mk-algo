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
import com.google.common.collect.Lists;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraph;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import mk.common.Pair;

import java.util.*;

import static com.google.common.base.Preconditions.checkState;

/**
 * <a href="https://en.wikipedia.org/wiki/Held%E2%80%93Karp_algorithm">Held–Karp algorithm</a> implementation of TSP
 * problem solver using a bottom-up dynamic programming approach with memoisation.
 *
 * <p>
 * Although it is much more efficient than naive approach, the implementation is aimed for educational purposes and
 * tries to be readable. It uses ordinary Java collection instead of more efficient low-level bit shifting.
 * </p>
 */
public final class HeldKarpTsp implements TspSolver {

    /** Let's utilise Guava's graph capabilities instead of the hand-made adjacency matrix. */
    private final ValueGraph<Integer, Integer> graph;

    /** Helper field keeping number of graph vertices (nodes). */
    private final int nOfVertices;

    private final Integer startingVertex;

    /**
     * Memoization structure which maps {@link Path} to the pair of cheapest parent vertex and the associated cost:
     * {@code Path -> <Vertex, Cost>}.
     */
    private final Map<Path, Pair<Integer, Integer>> minimumCosts;

    public HeldKarpTsp(ImmutableValueGraph<Integer, Integer> graph, int startingVertex) {
        this.graph = graph;
        this.nOfVertices = graph.nodes().size();
        checkState(startingVertex < nOfVertices, "starting vertex exists");
        // TODO: startingVertex is currently ignored. 0 is supposed to be starting vertex.
        this.startingVertex = 0;
        minimumCosts = new HashMap<>();
    }

    @Override
    public TspPath findBestPath() {
        List<Set<Integer>> powerSet = TspUtils.orderedPowerSet(nOfVertices - 1);
        for (Set<Integer> parents : powerSet) {
            for (int target = 1; target < nOfVertices; target++) { // all but starting vertex
                if (parents.contains(target)) { // target already visited
                    continue;
                }
                Path path = new Path(target, parents);
                if (parents.isEmpty()) { // parents is empty: direct path from starting vertex to target
                    minimumCosts.put(path, Pair.of(startingVertex, cost(target, startingVertex)));
                } else { // reach target from starting point via parents
                    findMinimumCost(path);
                }
            }
        }

        Set<Integer> allWithoutStart = copyWithoutElement(graph.nodes(), startingVertex);
        Path finalPath = new Path(startingVertex, allWithoutStart);
        findMinimumCost(finalPath);

        return new TspPath(
                reconstructPath(finalPath, Lists.newArrayList(startingVertex)),
                minimumCosts.get(finalPath).second);
    }

    /**
     * Finds a parent through which a path to the target has minimum cost.
     */
    private void findMinimumCost(Path path) {
        int minCost = Integer.MAX_VALUE;
        Integer minCostParent = null;
        for (Integer parent : path.parents) {
            // first look up cost to a parent via path with the parent removed
            int bestCostToParent = lookUpCostTo(parent, copyWithoutElement(path.parents, parent));
            int cost = cost(path.target, parent) + bestCostToParent;
            if (cost < minCost) {
                minCost = cost;
                minCostParent = parent;
            }
        }
        minimumCosts.put(path, Pair.of(minCostParent, minCost));
    }

    private ImmutableList<Integer> reconstructPath(Path path, List<Integer> pathAccumulator) {
        if (path.parents.isEmpty()) {
            pathAccumulator.add(0);
            return ImmutableList.copyOf(pathAccumulator);
        }

        Integer parent = minimumCosts.get(path).first;
        pathAccumulator.add(parent);
        Path pathToParent = new Path(parent, copyWithoutElement(path.parents, parent));
        return reconstructPath(pathToParent, pathAccumulator);
    }

    private int lookUpCostTo(Integer vertex, Set<Integer> viaPath) {
        return minimumCosts.get(new Path(vertex, viaPath)).second;
    }

    private Set<Integer> copyWithoutElement(Set<Integer> set, Integer exclude) {
        Set<Integer> result = new HashSet<>(set);
        result.remove(exclude);
        return result;
    }

    private Integer cost(int from, Integer to) {
        return graph.edgeValue(from, to).get();
    }

    /**
     * An ancillary data structure for a path to a vertex reachable via a set of other vertices in unspecified order.
     */
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class Path {

        /** Target vertex reached via the {@link #parents}. */
        private Integer target;

        /**
         * The set of vertices which were visited during the path to the {@link #target} vertex. Note that it is not
         * specified in which order the vertices in the {@code parents} were visited.
         */
        private Set<Integer> parents;

        @Override
        public String toString() {
            return "target: " + target + "; parents: " + parents;
        }

    }

}
