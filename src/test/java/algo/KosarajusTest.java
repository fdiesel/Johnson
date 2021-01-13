package algo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graphs.DirectedGraph;

class KosarajusTest {

	private Kosarajus<Integer> kosarajus;

	@BeforeEach
	void setUp() throws Exception {
		kosarajus = new Kosarajus<>();
	}

	@Test
	void testFindStronglyConnectedComponents() {

		DirectedGraph<Integer> graph = new DirectedGraph<>();

		graph.addEdge(0, 1);
		graph.addEdge(1, 0);
		graph.addEdge(1, 2);
		graph.addEdge(2, 1);
		graph.addEdge(2, 3);
		graph.addEdge(3, 2);
		graph.addEdge(3, 0);
		graph.addEdge(0, 3);

		graph.addEdge(2, 4);
		graph.addEdge(3, 5);
		graph.addEdge(4, 5);
		graph.addEdge(5, 4);

		List<DirectedGraph<Integer>> stronglyConnectedGraphs = kosarajus.getStronglyConnectedComponents(graph);

		assertEquals(2, stronglyConnectedGraphs.size());

		DirectedGraph<Integer> expectedGraph1 = new DirectedGraph<>();

		expectedGraph1.addEdge(0, 1);
		expectedGraph1.addEdge(1, 0);
		expectedGraph1.addEdge(1, 2);
		expectedGraph1.addEdge(2, 1);
		expectedGraph1.addEdge(2, 3);
		expectedGraph1.addEdge(3, 2);
		expectedGraph1.addEdge(3, 0);
		expectedGraph1.addEdge(0, 3);

		assertTrue(expectedGraph1.equals(stronglyConnectedGraphs.get(0)));

		DirectedGraph<Integer> expectedGraph2 = new DirectedGraph<>();

		expectedGraph2.addEdge(4, 5);
		expectedGraph2.addEdge(5, 4);

		assertTrue(expectedGraph2.equals(stronglyConnectedGraphs.get(1)));

	}

}
