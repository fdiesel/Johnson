package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import graphs.DirectedGraph;
import graphs.Vertex;

public class JohnsonRecursive<T> extends Johnson<T> {

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
				stack.push(startVertex);
				blockedSet.add(startVertex);
				explore(startVertex);
				currentGraph.removeVertex(startVertex.getId());
			}
		}

		return results;

	}

	private boolean explore(Vertex<T> currentVertex) {

		boolean foundCycle = false;

		for (Vertex<T> neighbour : currentVertex.getAdjacentVertices()) {
			if (neighbour.equals(startVertex)) {
				foundCycle = true;
				addCurrentStackToResults();
			} else if (blockedSet.contains(neighbour)) {
				addBlockade(currentVertex, neighbour);
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

		return foundCycle;

	}

}
