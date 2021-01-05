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

public class JohnsonIterative<T> implements Johnson<T> {

	Set<Vertex<T>> blockedSet = new HashSet<>();
	Map<Vertex<T>, Set<Vertex<T>>> blockedMap = new HashMap<>();
	Stack<Vertex<T>> stack = new Stack<>();
	List<DirectedGraph<T>> allCycles = new ArrayList<>();

	public List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph) {

		KosarajusAlgorithm<T> kosarajusAlgorithm = new KosarajusAlgorithm<>();

		// list of strongly connected subgraphs
		List<DirectedGraph<T>> stronglyConnectedSubGraphs = kosarajusAlgorithm.findStronglyConnectedComponents(graph);

		// for each strongly connected subgraph
		for (DirectedGraph<T> stronglyConnectedGraph : stronglyConnectedSubGraphs) {

			// while cycles can be found
			while (stronglyConnectedGraph.getVertexCount() > 1) {

				Vertex<T> startVertex = stronglyConnectedGraph.getAllVertices().get(0);

				blockedSet.add(startVertex);
				stack.push(startVertex);

				// search adjacent vertices
				outer: while (!stack.empty()) {

					Vertex<T> currentVertex = stack.peek();

					for (Vertex<T> adjacentVertex : currentVertex.getAdjacentVertices()) {

						// add child to stack if not in blocked set
						if (!blockedSet.contains(adjacentVertex)) {
							blockedSet.add(adjacentVertex);
							stack.push(adjacentVertex);
							continue outer;
						}

					}

					for (Vertex<T> adjacentVertex : currentVertex.getAdjacentVertices()) {

						// cycle found
						if (startVertex.equals(adjacentVertex)) {
							cycleFound();
						}

					}

					// pop if no child found
					stack.pop();

					if (blockedSet.contains(currentVertex)) {

						for (Vertex<T> adjacentVertex : currentVertex.getAdjacentVertices()) {

							blockedMap.putIfAbsent(adjacentVertex, new HashSet<>());
							blockedMap.get(adjacentVertex).add(currentVertex);

						}

					}

				}

				// remove vertex from graph
				stronglyConnectedGraph.removeVertex(startVertex.getId());

			}

		}

		return allCycles;

	}

	public void cycleFound() {

		Vertex<T> currentVertex = stack.peek();

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
		allCycles.add(cycleGraph);

		// unblock vertex so other cycles can be found
		unblock(currentVertex);
	}

	private void unblock(Vertex<T> vertex) {
		blockedSet.remove(vertex);
		if (blockedMap.containsKey(vertex)) {
			blockedMap.get(vertex).forEach(adjacentVertex -> {
				if (blockedSet.contains(adjacentVertex)) {
					unblock(adjacentVertex);
				}
			});
			blockedMap.remove(vertex);
		}
	}

}
