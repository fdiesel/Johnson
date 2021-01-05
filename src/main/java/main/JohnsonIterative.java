package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import graphs.DirectedGraph;
import graphs.Vertex;

public class JohnsonIterative<T> extends Johnson<T> {

	public List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph) {

		stack = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
		results = new ArrayList<>();

		// list of strongly connected subgraphs
		KosarajusAlgorithm<T> kos = new KosarajusAlgorithm<>();
		List<DirectedGraph<T>> subGraphs = kos.findStronglyConnectedComponents(graph);

		for (DirectedGraph<T> subGraph : subGraphs) {
			// while cycles can be found
			while (subGraph.getVertexCount() > 1) {

				currentGraph = subGraph;
				startVertex = subGraph.getAllVertices().get(0);
				stack.push(startVertex);
				blockedSet.add(startVertex);

				Stack<Vertex<T>> secondary = new Stack<>();
				secondary.push(startVertex);

				// search adjacent vertices
				while (!stack.empty()) {

					Vertex<T> currentVertex = stack.peek();

					for (Vertex<T> childVertex : currentVertex.getAdjacentVertices()) {
						if (childVertex.equals(startVertex)) {
							addCurrentStackToResults();
							stack.pop();
							unblock(currentVertex);
							if (!stack.empty()) {
								stack.pop();
							}
							if (!secondary.empty()) {
								unblock(secondary.remove(0));
							}
						} else if (blockedSet.contains(childVertex)) {
							addBlockade(currentVertex, childVertex);
							stack.pop();
							unblock(currentVertex);
							if (!stack.empty()) {
								stack.pop();
							}
							if (!secondary.empty()) {
								unblock(secondary.remove(0));
							}
						} else {
							stack.push(childVertex);
							blockedSet.add(childVertex);
							secondary.push(childVertex);
						}
					}

				}

				currentGraph.removeVertex(startVertex.getId());

			}

		}

		return results;

	}

}
