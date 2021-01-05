package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import graphs.DirectedGraph;
import graphs.Vertex;

public class JohnsonRecursive<T> implements Johnson<T> {

	private Stack<Vertex<T>> stack;
	private Set<Vertex<T>> blockedSet;
	private Map<Vertex<T>, Set<Vertex<T>>> blockedMap;
	private DirectedGraph<T> currentGraph;
	private Vertex<T> startVertex;
	private List<DirectedGraph<T>> results;

	public List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph) {
		stack = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
		results = new ArrayList<>();

		KosarajusAlgorithm<T> kos = new KosarajusAlgorithm<>();
		List<DirectedGraph<T>> subGraphs = kos.findStronglyConnectedComponents(graph);

		for (DirectedGraph<T> subGraph : subGraphs) {
			while (subGraph.getVertexCount() > 1) {
				currentGraph = subGraph;
				startVertex = subGraph.getAllVertices().get(0);
				stack.add(startVertex);
				blockedSet.add(startVertex);
				explore(startVertex);
				currentGraph.removeVertex(startVertex.getId());
			}
		}

		return results;
	}

	private void explore(Vertex<T> currentVertex) {
		for (Vertex<T> neighbour : currentVertex.getAdjacentVertices()) {
			if (neighbour.equals(startVertex)) {
				addCurrentStackToResults();
			} else if (blockedSet.contains(neighbour)) {
				addBlock(currentVertex, neighbour);
			} else {
				stack.add(neighbour);
				blockedSet.add(neighbour);
				explore(neighbour);
			}
		}
		Vertex<T> popped = stack.pop();
		assert popped.getId() == currentVertex.getId();
		unblock(currentVertex);
	}

	private void unblock(Vertex<T> v) {
		blockedSet.remove(v);
		Set<Vertex<T>> waitingList = blockedMap.get(v);
		if (waitingList == null)
			return;

		for (Vertex<T> waitingVertex : waitingList) {
			unblock(waitingVertex);
		}
		blockedMap.remove(v);
	}

	private void addBlock(Vertex<T> waiting, Vertex<T> blocking) {
		if (!blockedMap.containsKey(blocking)) {
			blockedMap.put(blocking, new HashSet<>());
		}
		blockedMap.get(blocking).add(waiting);
	}

	private void addCurrentStackToResults() {
		DirectedGraph<T> cycleGraph = new DirectedGraph<>();

		// create a graph based on stack
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
