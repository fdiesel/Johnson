package graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DirectedGraph<T> implements Cloneable {

	private List<Edge<T>> allEdges;
	private Map<Integer, Vertex<T>> allVertices;

	public DirectedGraph() {
		allEdges = new ArrayList<Edge<T>>();
		allVertices = new HashMap<Integer, Vertex<T>>();
	}

	public void addVertex(Vertex<T> vertex) {
		if (allVertices.containsKey(vertex.getId())) {
			return;
		}
		allVertices.put(vertex.getId(), vertex);
		for (Edge<T> edge : vertex.getEdges()) {
			allEdges.add(edge);
		}
	}

	public Vertex<T> addSingleVertex(int id) {
		if (allVertices.containsKey(id)) {
			return allVertices.get(id);
		}
		Vertex<T> v = new Vertex<T>(id);
		allVertices.put(id, v);
		return v;
	}

	// David (29.12.2020)
	public void removeVertex(Vertex<T> vertex) {
		List<Edge<T>> toDelete = vertex.getEdges();
		for (int i = 0; i < toDelete.size(); i++) {
			this.removeEdge(toDelete.get(i));
		}
		allVertices.remove(vertex.getId());
	}

	public Vertex<T> getVertex(int id) {
		return allVertices.get(id);
	}

	public void addEdge(int fromId, int toId) {
		Vertex<T> fromVertex = null;
		if (allVertices.containsKey(fromId)) {
			fromVertex = allVertices.get(fromId);
		} else {
			fromVertex = new Vertex<T>(fromId);
			allVertices.put(fromId, fromVertex);
		}
		Vertex<T> toVertex = null;
		if (allVertices.containsKey(toId)) {
			toVertex = allVertices.get(toId);
		} else {
			toVertex = new Vertex<T>(toId);
			allVertices.put(toId, toVertex);
		}
		Edge<T> edge = fromVertex.append(toVertex);
		allEdges.add(edge);
	}

	public void addEdge(Edge<T> edge) {
		addEdge(edge.getFrom(), edge.getTo());
	}

	public void addEdge(Vertex<T> from, Vertex<T> to) {
		addEdge(from.getId(), to.getId());
	}

	// David (29.12.2020)
	public void removeEdge(Edge<T> edge) {
		if (allEdges.contains(edge)) {
			allEdges.remove(edge);
			Vertex<T> vertex1 = edge.getFrom();
			Vertex<T> vertex2 = edge.getTo();
			vertex1.remove(vertex2.getId());
			vertex2.remove(vertex1.getId());
		}
	}

	public List<Edge<T>> getAllEdges() {
		return allEdges;
	}

	public Collection<Vertex<T>> getAllVertices() {
		return allVertices.values();
	}

	public int getVertexCount() {
		return allVertices.size();
	}

	public void setDataForVertex(int id, T data) {
		if (allVertices.containsKey(id)) {
			Vertex<T> vertex = allVertices.get(id);
			vertex.setData(data);
		}
	}

	public DirectedGraph<T> clone() {
		DirectedGraph<T> graph = new DirectedGraph<T>();
		this.getAllVertices().forEach(graph::addVertex);
		this.getAllEdges().forEach(graph::addEdge);
		return graph;
	}

	// David (29.12.2020)
	public DirectedGraph<T> reverse() {
		DirectedGraph<T> outputGraph = new DirectedGraph<T>();
		List<Edge<T>> edges = getAllEdges();
		for (int i = 0; i != edges.size(); i++) {
			Edge<T> edge = edges.get(i);
			Vertex<T> vertex1 = edge.getFrom();
			Vertex<T> vertex2 = edge.getTo();
			outputGraph.addEdge(vertex2, vertex1);
		}
		return outputGraph;
	}

	// David (29.12.2020)
	public DirectedGraph<T> getSubGraph(Set<Integer> idsToKeep) {

		// create empty graph
		DirectedGraph<T> graph = this.clone();
		int vertexCount = graph.getVertexCount();
		for (int i = 0; i != vertexCount; i++) {
			if (!idsToKeep.contains(i))
				graph.removeVertex(graph.getVertex(i));
		}
		return graph;
	}

	@Override
	public String toString() {
		return String.join("\n", getAllEdges().stream().map(edge -> edge.toString()).collect(Collectors.toList()));
	}

	public static DirectedGraph<Integer> fromString(String edgeString) {

		// edge string pattern
		final Pattern pattern = Pattern.compile(
				"\\{\\s*([0-9]+)\\s*,\\s*([0-9]+).*?\\}",
				Pattern.CASE_INSENSITIVE);

		// match the pattern in the file
		Matcher matcher = pattern.matcher(edgeString);

		// new directed graph
		DirectedGraph<Integer> graph = new DirectedGraph<>();

		// create vertices and edges
		while (matcher.find()) {

			// get pair
			Integer srcId = Integer.valueOf(matcher.group(1));
			Integer dstId = Integer.valueOf(matcher.group(2));

			// create vertices
			Vertex<Integer> src = new Vertex<>(srcId);
			Vertex<Integer> dst = new Vertex<>(dstId);

			// insert vertices
			graph.addVertex(src);
			graph.addVertex(dst);

			// insert edge
			graph.addEdge(src, dst);

		}

		return graph;
	}

	/// loads graph from file
	public static DirectedGraph<Integer> fromFile(String filename)
			throws InvalidPathException, IOException, SecurityException {

		Path path = Paths.get(filename);

		// concatenate all lines
		String line = String.join(" ", Files.readAllLines(path));

		return fromString(line);

	}

}
