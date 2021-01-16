package graphs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DirectedGraph<T> implements Cloneable {

	private Map<Integer, Vertex<T>> allVertices;

	/**
	 * Creates a new DirectedGraph
	 */
	public DirectedGraph() {
		allVertices = new HashMap<Integer, Vertex<T>>();
	}

	/**
	 * Adds a Vertex to the Graph, if it doesn't contain the Vertex yet
	 * 
	 * @param vertex to add
	 */
	public void addVertex(Vertex<T> vertex) {
		if (allVertices.containsKey(vertex.getId())) {
			return;
		}
		allVertices.put(vertex.getId(), vertex);
	}

	/**
	 * Create a Vertex with the given id and add it to the Graph
	 * 
	 * @param id to create the Vertex with
	 * @return created Vertex
	 */
	public Vertex<T> addSingleVertex(int id) {
		if (allVertices.containsKey(id)) {
			return allVertices.get(id);
		}
		Vertex<T> v = new Vertex<T>(id);
		allVertices.put(id, v);
		return v;
	}

	/**
	 * Sets the data of a Vertex by id if it exists
	 * 
	 * @param id   to search for
	 * @param data to set
	 */
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

	/**
	 * Returns a Vertex by ID
	 * 
	 * @param id to look for
	 * @return found Vertex
	 * @throws NullPointerException - if the ID was not found
	 */
	public Vertex<T> getVertex(int id) throws NullPointerException {
		return allVertices.get(id);
	}

	/**
	 * Get a list of all Vertices
	 * 
	 * @return list of Vertices
	 */
	public List<Vertex<T>> getAllVertices() {
		return new ArrayList<Vertex<T>>(allVertices.values());
	}

	/**
	 * Get the count of all Vertices
	 * 
	 * @return count of Vertices
	 */
	public int getVertexCount() {
		return allVertices.size();
	}

	/**
	 * Adds an Edge from id to id and creates the corresponding Vertices if they
	 * don't exist yet
	 * 
	 * @param fromId source Vertex id
	 * @param toId   destination Vertex id
	 */
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

	/**
	 * Adds the Vertices if they don't exist yet and creates an Edge between them
	 * 
	 * @param from
	 * @param to
	 */
	public void addEdge(Vertex<T> from, Vertex<T> to) {
		addVertex(from);
		addVertex(to);
		addEdge(from.getId(), to.getId());
	}

	/**
	 * Adds an Edge and inserts the corresponding Vertices if they
	 * don't exist yet
	 * 
	 * @param edge to add
	 */
	public void addEdge(Edge<T> edge) {
		addEdge(edge.getFrom(), edge.getTo());
	}

	/**
	 * Connects each Vertex in the chain to the next one and creates each Vertex
	 * that doesn't exist
	 * 
	 * @param ids of Vertices
	 */
	public void addTrack(int... ids) {
		for (int i = 0; i < ids.length - 1; i++)
			addEdge(ids[i], ids[i + 1]);
	}

	/**
	 * Removes the Edge in between the two Vertices if possible
	 * 
	 * @param fromId
	 * @param toId
	 */
	public void removeEdge(int fromId, int toId) {
		if (allVertices.containsKey(fromId)) {
			allVertices.get(fromId).remove(toId);
		}
	}

	/**
	 * Get a list of all Edges
	 * 
	 * @return list of Edges
	 */
	public List<Edge<T>> getAllEdges() {
		return allVertices.values().stream()
				.map(vertex -> vertex.getEdges())
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	/**
	 * Creates a deep copy of the Graph
	 * 
	 * @return copied Graph
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

	/**
	 * Deep copy the Graph and reverse all edges
	 * 
	 * @return reversed Graph
	 */
	public DirectedGraph<T> getReversed() {
		DirectedGraph<T> outputGraph = new DirectedGraph<T>();
		List<Edge<T>> edges = getAllEdges();
		for (int i = 0; i != edges.size(); i++) {
			Edge<T> edge = edges.get(i);
			Vertex<T> vertex1 = edge.getFrom().cloneWithoutEdges();
			Vertex<T> vertex2 = edge.getTo().cloneWithoutEdges();
			outputGraph.addEdge(vertex2, vertex1);
		}
		return outputGraph;
	}

	/**
	 * Get a deep copy of the Graph, with the specified IDs
	 * 
	 * @param idsToKeep
	 * @return Subgraph
	 */
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
	 * Creates a Subgraph containing all the Vertices with an ID higher than
	 * minVertexId
	 * 
	 * @param minVertexId minimum ID to look for
	 * @return Subgraph
	 */
	public DirectedGraph<T> getSubGraph(int minVertexId) {

		Set<Integer> idsToKeep = getAllVertices()
				.stream()
				.map(vertex -> vertex.getId())
				.filter(id -> id >= minVertexId)
				.collect(Collectors.toSet());

		return getSubGraph(idsToKeep);

	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("Vertices: ");
		buffer.append(getAllVertices().toString());
		buffer.append("\n");
		buffer.append("Edges:");

		getAllEdges().forEach(edge -> {
			buffer.append("\n");
			buffer.append(edge.toString());
		});

		return buffer.toString();
	}

	/**
	 * Checks this Graph equals another Directed Graph
	 * 
	 * @param otherGraph to compare to
	 * @return true if there is the same amount of Edges, Vertices and the IDs are
	 *         equal
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		DirectedGraph other = (DirectedGraph) obj;

		if (this.getVertexCount() != other.getVertexCount())
			return false;

		List<Edge<T>> myEdges = this.getAllEdges();
		List<Edge<T>> otherEdges = other.getAllEdges();

		if (myEdges.size() != otherEdges.size())
			return false;

		for (Edge<T> myEdge : myEdges) {
			if (otherEdges.stream().noneMatch(myEdge::equals))
				return false;
		}

		return true;
	}

	/**
	 * Creates a Graph form a String containing Edges<br>
	 * Format: {&lt;from ID&gt;, &lt;to ID&gt;}<br>
	 * Separator: any
	 * 
	 * @param edgesString
	 * @return DirectedGraph
	 */
	public static DirectedGraph<Integer> fromString(String edgesString) {

		// edge string pattern
		final Pattern pattern = Pattern.compile(
				"\\{\\s*([0-9]+)\\s*,\\s*([0-9]+).*?\\}",
				Pattern.CASE_INSENSITIVE);

		// match the pattern in the file
		Matcher matcher = pattern.matcher(edgesString);

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

	/**
	 * Creates a Directed Graph from a file
	 * 
	 * @param filename
	 * 
	 * @return Directed Graph
	 * 
	 * @throws InvalidPathException - if the filename string cannot be converted to
	 *                              a Path
	 * @throws IOException          - if an I/O error occurs reading from the file
	 *                              or a malformed or unmappable byte sequence is
	 *                              read
	 * @throws SecurityException    - In the case of the default provider, and a
	 *                              security manager is installed, the checkRead
	 *                              method is invoked to check read access to the
	 *                              file.
	 */
	public static DirectedGraph<Integer> fromFile(String filename)
			throws InvalidPathException, IOException, SecurityException {

		Path path = Paths.get(filename);

		// concatenate all lines
		String line = String.join("\n", Files.readAllLines(path));

		return fromString(line);

	}

}
