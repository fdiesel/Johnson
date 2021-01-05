package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import graphs.DirectedGraph;

public abstract class JohnsonTestBase<T extends Johnson<Integer>> {

    private T johnson;

    protected abstract T createJohnsonInstance();

    @BeforeEach
    public void setUp() {
        johnson = createJohnsonInstance();
    }

    @Test
    void testNoCycle() {
        List<DirectedGraph<Integer>> expectedGraphs = new ArrayList<>();

        performTestForFile("testGraphs/noCycle.txt", expectedGraphs);
    }

    @Test
    void testOneCycle() {
        List<DirectedGraph<Integer>> expectedGraphs = new ArrayList<>();
        expectedGraphs.add(DirectedGraph.fromString("{1,2}\n{2,3}\n{3,9}\n{9,1}"));

        performTestForFile("testGraphs/oneCycle.txt", expectedGraphs);
    }

    @Test
    void testThreeCycles() {
        List<DirectedGraph<Integer>> expectedGraphs = new ArrayList<>();
        expectedGraphs.add(DirectedGraph.fromString("{2,3}\n{3,2}"));
        expectedGraphs.add(DirectedGraph.fromString("{1,2}\n{2,3}\n{3,1}"));
        expectedGraphs.add(DirectedGraph.fromString("{1,2}\n{2,3}\n{3,9}\n{9,1}"));

        performTestForFile("testGraphs/threeCycles.txt", expectedGraphs);
    }

    private void performTestForFile(String graphFilePath, List<DirectedGraph<Integer>> expectedGraphs) {
        try {
            DirectedGraph<Integer> graph = DirectedGraph.fromFile(graphFilePath);
            List<DirectedGraph<Integer>> resultingCycleGraphs = johnson.getCycles(graph);

            boolean graphsEqual = expectedGraphs.size() == resultingCycleGraphs.size();
            for (DirectedGraph<Integer> expectedGraph : expectedGraphs) {
                if (resultingCycleGraphs.stream().noneMatch(result -> result.equals(expectedGraph))) {
                    graphsEqual = false;
                    break;
                }
            }
            String errorMessage = "Expected cycle graphs and actual cycle graphs do not match.\nExpected:\n[" +
                    expectedGraphs.stream().map(Object::toString)
                            .collect(Collectors.joining("],\n[ "))
                    +
                    "]\nbut was: [" +
                    resultingCycleGraphs.stream().map(Object::toString)
                            .collect(Collectors.joining("]\n,[ "))
                    + "]";

            assertTrue(graphsEqual, errorMessage);
        } catch (Exception e) {
            fail(e);
        }
    }

}
