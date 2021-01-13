/**
 * 
 */
package graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectedGraphTest {

	@BeforeEach
	void setUp() throws Exception {}

	@Test
	void testClone() {

		DirectedGraph<Integer> sourceGraph = new DirectedGraph<>();
		Vertex<Integer> vertex1 = new Vertex<>(0);
		Vertex<Integer> vertex2 = new Vertex<>(1);
		sourceGraph.addVertex(vertex1);
		sourceGraph.addVertex(vertex2);
		sourceGraph.addEdge(0, 1);

		DirectedGraph<Integer> clonedGraph = sourceGraph.clone();
		sourceGraph.removeVertex(1);

		assertEquals(2, clonedGraph.getVertexCount());

	}

	@Test
	void testReverse() {

		DirectedGraph<Integer> sourceGraph = new DirectedGraph<>();
		sourceGraph.addEdge(0, 1);
		sourceGraph.addEdge(1, 2);
		sourceGraph.addEdge(2, 1);

		DirectedGraph<Integer> actualReversedGraph = sourceGraph.getReversed();

		DirectedGraph<Integer> expectedReversedGraph = new DirectedGraph<>();
		expectedReversedGraph.addEdge(1, 0);
		expectedReversedGraph.addEdge(1, 2);
		expectedReversedGraph.addEdge(2, 1);

		assertTrue(actualReversedGraph.equals(expectedReversedGraph));

	}

	@Test
	void testEqualsObject() {

		DirectedGraph<Integer> graph1 = new DirectedGraph<>();
		graph1.addEdge(0, 1);
		graph1.addEdge(1, 2);
		graph1.addEdge(2, 0);

		DirectedGraph<Integer> graph2 = new DirectedGraph<>();
		graph2.addEdge(0, 1);
		graph2.addEdge(1, 2);
		graph2.addEdge(2, 0);

		assertTrue(graph1.equals(graph2));

	}

	@Test
	void testNotEqualsObject() {

		DirectedGraph<Integer> graph1 = new DirectedGraph<>();
		graph1.addEdge(0, 1);
		graph1.addEdge(1, 2);
		graph1.addEdge(2, 0);

		DirectedGraph<Integer> graph2 = new DirectedGraph<>();
		graph2.addEdge(0, 1);
		graph2.addEdge(1, 2);
		graph2.addEdge(0, 2);

		assertFalse(graph1.equals(graph2));

	}

	@Test
	void testGetSubGraph() {

		DirectedGraph<Integer> graph = new DirectedGraph<>();
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);

		Set<Integer> idsToKeep = new HashSet<>();
		idsToKeep.add(1);
		idsToKeep.add(2);
		idsToKeep.add(3);

		DirectedGraph<Integer> actualSubGraph = graph.getSubGraph(idsToKeep);

		DirectedGraph<Integer> expectedSubGraph = new DirectedGraph<>();
		expectedSubGraph.addEdge(1, 2);
		expectedSubGraph.addEdge(2, 3);

		assertTrue(actualSubGraph.equals(expectedSubGraph));

	}

	@Test
	void testGetMinSubGraph() {

		DirectedGraph<Integer> graph = new DirectedGraph<>();
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);

		DirectedGraph<Integer> actualSubGraph = graph.getSubGraph(1);

		DirectedGraph<Integer> expectedSubGraph = new DirectedGraph<>();
		expectedSubGraph.addEdge(1, 2);
		expectedSubGraph.addEdge(2, 3);

		assertTrue(actualSubGraph.equals(expectedSubGraph));

	}

}
