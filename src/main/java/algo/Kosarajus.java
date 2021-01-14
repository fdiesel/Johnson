package algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import graphs.DirectedGraph;
import graphs.Vertex;

/**
 * Kosarajus algorithm to find strongly connected components in a graph
 *
 * @param <T> type of graph to work on
 */
public class Kosarajus<T> {

	// stores the vertices in order of finishing depth search
	private Deque<Integer> stack;
	// stores info if vertex has already been visited
	private Set<Integer> visited;
	// set of vertexIDs that represent a strongly connected component
	private Set<Integer> set;
	// list of strongly connected components found by algorithm
	private List<Set<Integer>> result = new ArrayList<>();
	DirectedGraph<T> graph;
	DirectedGraph<T> reversedGraph;

	/**
	 * Method to find all strongly-connected components in a directed graph
	 * 
	 * @param inputGraph
	 * @return Returns a list of subgraphs that represent the strongly-connected
	 *         components
	 */
	public List<DirectedGraph<T>> getStronglyConnectedComponents(DirectedGraph<T> inputGraph) {

		// clone graph to not modify inputGraph
		graph = inputGraph.clone();
		reversedGraph = graph.getReversed();
		result.clear();
		visited = new HashSet<>();
		stack = new ArrayDeque<>();

		// depthSearch using buildStack on all vertices not visited
		for (Vertex<T> vertex : graph.getAllVertices()) {
			if (!visited.contains(vertex.getId())) {
				buildStack(vertex.getId());
			}
		}

		// second part of algorithm begins

		// set all vertices to not visited
		visited.clear();

		// depthSearch using buildSet on all not visited Elements of stack in order
		while (!stack.isEmpty()) {
			int current = stack.removeLast();
			if (!visited.contains(current)) {
				set = new HashSet<>();
				buildSet(current);
				result.add(set);
			}
		}

		return result.stream()
				.map(set -> graph.getSubGraph(set))
				.collect(Collectors.toList());
	}

	/**
	 * Recursively save graph to Stack by depth first search
	 * 
	 * @param vertexId
	 */
	private void buildStack(int vertexId) {
		visited.add(vertexId);
		for (Vertex<T> current : graph.getVertex(vertexId).getAdjacentVertices()) {
			int currentID = current.getId();
			if (!visited.contains(currentID)) {
				buildStack(currentID);
			}
		}
		stack.add(vertexId);
	}

	/**
	 * Recursively save reversed Graph to Set by depth first search
	 * 
	 * @param vertexId to start from
	 */
	private void buildSet(int vertexId) {
		visited.add(vertexId);
		set.add(vertexId);
		for (Vertex<T> current : reversedGraph.getVertex(vertexId).getAdjacentVertices()) {
			int currentID = current.getId();
			if (!visited.contains(currentID)) {
				buildSet(currentID);
			}
		}
	}

}
