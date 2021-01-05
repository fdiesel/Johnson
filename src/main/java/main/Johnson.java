package main;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import graphs.DirectedGraph;
import graphs.Vertex;

public abstract class Johnson<T> {

	protected Stack<Vertex<T>> stack;
	protected Set<Vertex<T>> blockedSet;
	protected Map<Vertex<T>, Set<Vertex<T>>> blockedMap;
	protected DirectedGraph<T> currentGraph;
	protected Vertex<T> startVertex;
	protected List<DirectedGraph<T>> results;

	public abstract List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph);

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
