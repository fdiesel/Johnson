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

public class Johnson<T> {

	protected Stack<Vertex<T>> stack;
	protected Set<Vertex<T>> blockedSet;
	protected Map<Vertex<T>, Set<Vertex<T>>> blockedMap;
	protected DirectedGraph<T> currentGraph;
	protected Vertex<T> startVertex;
	protected List<DirectedGraph<T>> results;

	public List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph) {

		stack = new Stack<>();
		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
		results = new ArrayList<>();

		Kosarajus<T> kos = new Kosarajus<>();
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
		else {
			// if any neighbour gets unblocked, this node should get unblocked as well
			for (Vertex<T> neighbour : currentVertex.getAdjacentVertices()) {
				addBlockade(currentVertex, neighbour);
			}
		}

		return foundCycle;

	}

	protected void unblock(Vertex<T> v) {
		blockedSet.remove(v);
		Set<Vertex<T>> waitingList = blockedMap.get(v);
		if (waitingList == null)
			return;

		for (Vertex<T> waitingVertex : waitingList) {
			unblock(waitingVertex);
		}
		blockedMap.remove(v);
	}

	protected void addBlockade(Vertex<T> waiting, Vertex<T> blocking) {
		if (!blockedMap.containsKey(blocking)) {
			blockedMap.put(blocking, new HashSet<>());
		}
		blockedMap.get(blocking).add(waiting);
	}

	protected void addCurrentStackToResults() {
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
