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

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TspUtils {

    /**
     * @param n number of elements in the source set
     * @return ordered power set of the (1..n) set <b>excluding</b> (last) full subset element [1, 2, .., n] element.
     *         Suitable for our specific purposes.
     */
    static List<Set<Integer>> orderedPowerSet(int n) {
        Set<Integer> ints = IntStream.range(1, n + 1).boxed().collect(Collectors.toSet());
        List<Set<Integer>> powerSet = new ArrayList<>(Sets.powerSet(ints));
        Collections.sort(powerSet, Ordering.natural().onResultOf(Set::size));
        powerSet.remove(powerSet.size() - 1);
        return powerSet;
    }

}
