package graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DirectedGraph<T> implements Cloneable {

	private Map<Integer, Vertex<T>> allVertices;

	public DirectedGraph() {
		allVertices = new HashMap<Integer, Vertex<T>>();
	}

	public void addVertex(Vertex<T> vertex) {
		if (allVertices.containsKey(vertex.getId())) {
			return;
		}
		allVertices.put(vertex.getId(), vertex);
	}

	public Vertex<T> addSingleVertex(int id) {
		if (allVertices.containsKey(id)) {
			return allVertices.get(id);
		}
		Vertex<T> v = new Vertex<T>(id);
		allVertices.put(id, v);
		return v;
	}

	public void setDataForVertex(int id, T data) {
		if (allVertices.containsKey(id)) {
			Vertex<T> vertex = allVertices.get(id);
			vertex.setData(data);
		}
	}

	/**
	 * Removes a Vertex and all its Edges
	 * 
	 * @param id of the Vertex to remove
	 */
	public void removeVertex(int id) {

		// remove Vertex as adjacent Vertex from all connected Vertices
		getAllEdges().stream()
				.filter(edge -> edge.getTo().getId() == id)
				.forEach(edge -> edge.getFrom().remove(id));

		// remove Vertex form Graph
		allVertices.remove(id);

	}

	public Vertex<T> getVertex(int id) {
		return allVertices.get(id);
	}

	public Collection<Vertex<T>> getAllVertices() {
		return allVertices.values();
	}

	public int getVertexCount() {
		return allVertices.size();
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
		fromVertex.append(toVertex);
	}

	public void addEdge(Edge<T> edge) {
		addEdge(edge.getFrom(), edge.getTo());
	}

	public void addEdge(Vertex<T> from, Vertex<T> to) {
		addEdge(from.getId(), to.getId());
	}

	public void removeEdge(int fromId, int toId) {
		if (allVertices.containsKey(fromId)) {
			allVertices.get(fromId).remove(toId);
		}
	}

	public List<Edge<T>> getAllEdges() {
		return allVertices.values().stream()
				.map(vertex -> vertex.getEdges())
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	/**
	 * Creates a deep copy of all Vertices and reconnects them
	 * 
	 * @return deep copy of the Graph
	 */
	public DirectedGraph<T> clone() {

		DirectedGraph<T> graphCopy = new DirectedGraph<T>();

		// deep copy Vertices
		this.getAllVertices().forEach(vertex -> {
			Vertex<T> vertexCopy = new Vertex<T>(vertex.getId());
			if (vertex.hasData())
				vertexCopy.setData(vertex.getData());
			graphCopy.addVertex(vertexCopy);
		});

		// deep copy Edges
		this.getAllEdges().forEach(edge -> {
			graphCopy.addEdge(edge.getFrom().getId(), edge.getTo().getId());
		});

		return graphCopy;
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

		// clone graph
		DirectedGraph<T> graph = this.clone();

		// remove not needed Vertices
		for (Vertex<T> vertex : getAllVertices()) {
			if (!idsToKeep.contains(vertex.getId()))
				graph.removeVertex(vertex.getId());
		}

		return graph;
	}

	/**
	 * Creates a SubGraph containing all the Vertices with an ID higher than
	 * minVertexId
	 * 
	 * @param minVertexId minimum id to look for
	 * @return SubGraph
	 */
	public DirectedGraph<T> getSubGraph(int minVertexId) {

		// create empty subgraph
		DirectedGraph<T> subGraph = new DirectedGraph<>();

		// add all Edges to the graph with ids >= minVertexId
		for (Edge<T> edge : this.getAllEdges()) {
			if (edge.getFrom().getId() >= minVertexId && edge.getTo().getId() >= minVertexId) {
				subGraph.addEdge(edge.getFrom().getId(), edge.getTo().getId());
			}
		}

		return subGraph;
	}

	@Override
	public String toString() {
		return allVertices.values().toString() + "\n"
				+ String.join("\n", getAllEdges().stream().map(edge -> edge.toString()).collect(Collectors.toList()));
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
