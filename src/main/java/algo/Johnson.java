package algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import graphs.DirectedGraph;
import graphs.Vertex;

/**
 * Johnsons algorithm to find cycles in a graph
 *
 * @param <T> graph type to work on
 */
public class Johnson<T> {

	private Stack<Vertex<T>> stack;
	private Set<Vertex<T>> blockedSet;
	private Map<Vertex<T>, Set<Vertex<T>>> blockedMap;
	private DirectedGraph<T> currentGraph;
	private Vertex<T> startVertex;
	private List<DirectedGraph<T>> results;

	/**
	 * Finds all the cycles in a graph<br>
	 * Can be reused, on multiple independent graphs
	 * 
	 * @param graph to search for cycles
	 * @return list of found cycles
	 */
	public List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph) {

		stack = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
		results = new ArrayList<>();

		Kosarajus<T> kos = new Kosarajus<>();
		List<DirectedGraph<T>> subGraphs = kos.getStronglyConnectedComponents(graph);

		for (DirectedGraph<T> subGraph : subGraphs) {
			while (subGraph.getVertexCount() > 1) {
				currentGraph = subGraph;
				startVertex = subGraph.getAllVertices().get(0);
				stack.push(startVertex);
				blockedSet.add(startVertex);
				explore(startVertex);
				currentGraph.removeVertex(startVertex.getId());
			}
		}

		return results;

	}

	/**
	 * Recursive method to traverse the graph and find cycles
	 * 
	 * @param currentVertex
	 * @return true if a cycle has been found
	 */
	private boolean explore(Vertex<T> currentVertex) {

		boolean foundCycle = false;

		for (Vertex<T> neighbour : currentVertex.getAdjacentVertices()) {
			if (neighbour.equals(startVertex)) {
				foundCycle = true;
				addCurrentStackToResults();
			} else if (blockedSet.contains(neighbour)) {
				addBlockade(neighbour, currentVertex);
			} else {
				stack.push(neighbour);
				blockedSet.add(neighbour);
				boolean foundCycleInNeighbour = explore(neighbour);
				foundCycle = foundCycle || foundCycleInNeighbour;

			}
		}
		stack.pop();

		if (foundCycle)
			unblock(currentVertex);
		else {
			// if any neighbour gets unblocked, this node should get unblocked as well
			for (Vertex<T> neighbour : currentVertex.getAdjacentVertices()) {
				addBlockade(neighbour, currentVertex);
			}
		}

		return foundCycle;

	}

	/**
	 * Removes the Vertex from the blocked set and map
	 * 
	 * @param vertex
	 */
	private void unblock(Vertex<T> vertex) {
		blockedSet.remove(vertex);
		Set<Vertex<T>> waitingList = blockedMap.get(vertex);
		if (waitingList == null)
			return;
		for (Vertex<T> waitingVertex : waitingList)
			unblock(waitingVertex);
		blockedMap.remove(vertex);
	}

	/**
	 * Adds a Vertex which blocks another one to the blocked map
	 * 
	 * @param blockingVertex
	 * @param blockedVertex
	 */
	private void addBlockade(Vertex<T> blockingVertex, Vertex<T> blockedVertex) {
		if (!blockedMap.containsKey(blockingVertex)) {
			blockedMap.put(blockingVertex, new HashSet<>());
		}
		blockedMap.get(blockingVertex).add(blockedVertex);
	}

	/**
	 * Adds the current stack to the results as a cycle
	 */
	private void addCurrentStackToResults() {

		DirectedGraph<T> cycleGraph = new DirectedGraph<>();

		// create a graph (cycle) from stack with deep copied Vertices
		Vertex<T> from = stack.get(0).cloneWithoutEdges();
		int firstId = from.getId();
		cycleGraph.addVertex(from);
		for (int i = 1; i < stack.size(); i++) {
			Vertex<T> to = stack.get(i).cloneWithoutEdges();
			cycleGraph.addVertex(to);
			cycleGraph.addEdge(from.getId(), to.getId());
			from = to;
		}
		cycleGraph.addEdge(from.getId(), firstId);

		// save Graph to found cycles
		results.add(cycleGraph);

	}

}
