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
	void testFindFourStronglyConnectedComponents() {

		DirectedGraph<Integer> graph = new DirectedGraph<>();

		graph.addTrack(1, 2, 0, 1);

		graph.addTrack(1, 3);

		graph.addTrack(3, 4, 5, 3);

		graph.addTrack(6, 5);

		graph.addTrack(6, 7, 8, 9, 6);

		graph.addTrack(9, 10);

		List<DirectedGraph<Integer>> cycles = kosarajus.getStronglyConnectedComponents(graph);

		assertEquals(4, cycles.size());

		DirectedGraph<Integer> cycle1 = new DirectedGraph<>();
		cycle1.addTrack(1, 2, 0, 1);
		assertTrue(cycles.contains(cycle1));

		DirectedGraph<Integer> cycle2 = new DirectedGraph<>();
		cycle2.addTrack(3, 4, 5, 3);
		assertTrue(cycles.contains(cycle2));

		DirectedGraph<Integer> cycle3 = new DirectedGraph<>();
		cycle3.addTrack(6, 7, 8, 9, 6);
		assertTrue(cycles.contains(cycle3));

		DirectedGraph<Integer> cycle4 = new DirectedGraph<>();
		cycle4.addSingleVertex(10);
		assertTrue(cycles.contains(cycle4));

	}

	@Test
	void testFindTwoStronglyConnectedComponents() {

		DirectedGraph<Integer> graph = new DirectedGraph<>();

		graph.addTrack(0, 1, 2, 3, 0);
		graph.addTrack(0, 3, 2, 1, 0);

		graph.addEdge(2, 4);
		graph.addEdge(3, 5);

		graph.addTrack(4, 5, 4);

		List<DirectedGraph<Integer>> stronglyConnectedGraphs = kosarajus.getStronglyConnectedComponents(graph);

		assertEquals(2, stronglyConnectedGraphs.size());

		DirectedGraph<Integer> expectedGraph1 = new DirectedGraph<>();

		expectedGraph1.addTrack(0, 1, 2, 3, 0);
		expectedGraph1.addTrack(0, 3, 2, 1, 0);

		assertTrue(expectedGraph1.equals(stronglyConnectedGraphs.get(0)));

		DirectedGraph<Integer> expectedGraph2 = new DirectedGraph<>();

		expectedGraph2.addTrack(4, 5, 4);

		assertTrue(expectedGraph2.equals(stronglyConnectedGraphs.get(1)));

	}

}
