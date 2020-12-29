package de.floriandiesel.Johnson;

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

public class Graph<T> implements Cloneable {

	private List<Edge<T>> allEdges;
	private Map<Long, Vertex<T>> allVertices;

	public Graph() {
		allEdges = new ArrayList<Edge<T>>();
		allVertices = new HashMap<Long, Vertex<T>>();
	}

	public void addEdge(long fromId, long toId) {
		addEdge(fromId, toId, 0);
	}

	public void addEdge(Vertex<T> from, Vertex<T> to) {
		addEdge(from.id, to.id);
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

	// David (29.12.2020)
	public void removeVertex(Vertex<T> vertex) {
		List<Edge<T>> toDelete = vertex.getEdges();
		for (int i = 0; i < toDelete.size(); i++) {
			this.removeEdge(toDelete.get(i));
		}
		allVertices.remove(vertex.getId());
	}

	public Vertex<T> addSingleVertex(long id) {
		if (allVertices.containsKey(id)) {
			return allVertices.get(id);
		}
		Vertex<T> v = new Vertex<T>(id);
		allVertices.put(id, v);
		return v;
	}

	public Vertex<T> getVertex(long id) {
		return allVertices.get(id);
	}

	public void addEdge(long fromId, long toId, int weight) {
		Vertex<T> vertex1 = null;
		if (allVertices.containsKey(fromId)) {
			vertex1 = allVertices.get(fromId);
		} else {
			vertex1 = new Vertex<T>(fromId);
			allVertices.put(fromId, vertex1);
		}
		Vertex<T> vertex2 = null;
		if (allVertices.containsKey(toId)) {
			vertex2 = allVertices.get(toId);
		} else {
			vertex2 = new Vertex<T>(toId);
			allVertices.put(toId, vertex2);
		}

		Edge<T> edge = new Edge<T>(vertex1, vertex2, weight);
		allEdges.add(edge);
		vertex1.addAdjacentVertex(edge, vertex2);

	}

	public void addEdge(Edge<T> edge) {
		addEdge(edge.getFrom(), edge.getTo());
	}

	// David (29.12.2020)
	public void removeEdge(Edge<T> edge) {
		if (allEdges.contains(edge)) {
			allEdges.remove(edge);
			Vertex<T> vertex1 = edge.getFrom();
			Vertex<T> vertex2 = edge.getTo();
			vertex1.removeAdjacentVertex(edge, vertex2);
			vertex2.removeAdjacentVertex(edge, vertex1);
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

	public void setDataForVertex(long id, T data) {
		if (allVertices.containsKey(id)) {
			Vertex<T> vertex = allVertices.get(id);
			vertex.setData(data);
		}
	}

	public Graph<T> clone() {
		Graph<T> graph = new Graph<T>();
		this.getAllVertices().forEach(graph::addVertex);
		this.getAllEdges().forEach(graph::addEdge);
		return graph;
	}

	// David (29.12.2020)
	public Graph<T> reverse() {
		Graph<T> outputGraph = new Graph<T>();
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
	public Graph<T> getSubGraph(Set<Long> idsToKeep) {

		// create empty graph
		Graph<T> graph = this.clone();
		int vertexCount = graph.getVertexCount();
		for (int i = 0; i != vertexCount; i++) {
			if (!idsToKeep.contains((long) i))
				graph.removeVertex(graph.getVertex(i));
		}
		return graph;
	}

	@Override
	public String toString() {
		return String.join("\n", getAllEdges().stream().map(edge -> edge.toString()).collect(Collectors.toList()));
	}

	public static Graph<Integer> fromString(String edgeString) {

		// edge string pattern
		final Pattern pattern = Pattern.compile(
				"\\{\\s*([0-9]+)\\s*,\\s*([0-9]+).*?\\}",
				Pattern.CASE_INSENSITIVE);

		// match the pattern in the file
		Matcher matcher = pattern.matcher(edgeString);

		// new directed graph
		Graph<Integer> graph = new Graph<>();

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
	public static Graph<Integer> fromFile(String filename) throws InvalidPathException, IOException, SecurityException {

		Path path = Paths.get(filename);

		// concatenate all lines
		String line = Files.readAllLines(path)
				.stream()
				.reduce("", (a, b) -> a + b);

		return fromString(line);

	}

}

class Vertex<T> {

	long id;
	private T data;
	private List<Edge<T>> edges = new ArrayList<>();
	private List<Vertex<T>> adjacentVertex = new ArrayList<>();

	Vertex(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	// David (29.12.2020)
	public int adjacentVertexCount() {
		return adjacentVertex.size();
	}

	public void addAdjacentVertex(Edge<T> e, Vertex<T> v) {
		edges.add(e);
		adjacentVertex.add(v);
	}

	// David (29.12.2020)
	public void removeAdjacentVertex(Edge<T> e, Vertex<T> v) {
		edges.remove(e);
		adjacentVertex.remove(v);
	}

	public String toString() {
		return String.valueOf(id);
	}

	public List<Vertex<T>> getAdjacentVertices() {
		return adjacentVertex;
	}

	public List<Edge<T>> getEdges() {
		return edges;
	}

	public int getDegree() {
		return edges.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex<T> other = (Vertex<T>) obj;
		if (id != other.id)
			return false;
		return true;
	}

}

class Edge<T> {

	private Vertex<T> from;
	private Vertex<T> to;
	private int weight;

	Edge(Vertex<T> vertex1, Vertex<T> vertex2) {
		this.from = vertex1;
		this.to = vertex2;
		this.weight = 0;
	}

	Edge(Vertex<T> vertex1, Vertex<T> vertex2, int weight) {
		this.from = vertex1;
		this.to = vertex2;
		this.weight = weight;
	}

	Vertex<T> getFrom() {
		return from;
	}

	Vertex<T> getTo() {
		return to;
	}

	int getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge<T> other = (Edge<T>) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("(%2d -- %-2d)", from.id, to.id);
	}

}
