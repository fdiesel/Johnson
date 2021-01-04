package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphs.DirectedGraph;
import graphs.Vertex;

public class KosarajusAlgorithm {

	// stores the vertices in order of finishing depth search
	Deque<Integer> stack;
	// stores info if vertex has already been visited
	Set<Integer> visited;
	// list of strongly connected components found by algorithm
	List<Set<Integer>> result = new ArrayList<>();
	// stores info if algorithm is in second part (changes a bit of depthSearch)
	boolean afterReverse;

	public List<DirectedGraph<Integer>> findStronglyConnectedComponents(DirectedGraph<Integer> inputGraph) {

		// clone graph to not modify inputGraph
		DirectedGraph<Integer> graph = inputGraph.clone();
		result.clear();
		afterReverse = false;
		int vertices = graph.getVertexCount();
		visited = new HashSet<>();
		stack = new ArrayDeque<>();

		// depthSearch on all vertices not visited
		for (Vertex<Integer> vertex : graph.getAllVertices()) {
			if (!visited.contains(vertex.getId())) {
				// null input because not needed before (afterReverse = true)
				depthSearch(vertex.getId(), graph, null);
			}
		}

		// reverse graph, second part of algorithm begins
		graph = graph.reverse();
		afterReverse = true; // Changes the functionality of depthSearch
		// set all vertices to not visited
		visited.clear();

		// depthSearch on all not visited Elements of stack in order
		while (!stack.isEmpty()) {
			int current = stack.removeLast();
			if (!visited.contains(current)) {
				Set<Integer> set = new HashSet<>();
				depthSearch(current, graph, set);
				result.add(set);
			}
		}

		List<DirectedGraph<Integer>> subGraphs = new ArrayList<DirectedGraph<Integer>>();

		result.forEach(set -> subGraphs.add(inputGraph.getSubGraph(set)));

		return subGraphs;
	}

	public void depthSearch(int vertexId, DirectedGraph<Integer> graph, Set<Integer> set) {
		visited.add(vertexId);
		if (afterReverse)
			set.add(vertexId); // if in part 2 of algo build strongly connected component in set
		// for each adjacent vertex not visited do depthSearch (= recursive
		// implementation of depthSearch)
		for (Vertex<Integer> current : graph.getVertex(vertexId).getAdjacentVertices()) {
			int currentID = current.getId();
			if (!visited.contains(currentID)) {
				depthSearch(currentID, graph, set);
			}
		}
		if (!afterReverse)
			stack.add(vertexId); // if in part 1 of algo build stack
	}

}
