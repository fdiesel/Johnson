package graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Consists of an ID, data and outgoing connected Edges
 *
 * @param <T> type of the data
 */
public class Vertex<T> {

	private int id;
	private Optional<T> data;
	private List<Edge<T>> edges;

	/**
	 * Creates a new Vertex
	 * 
	 * @param id   of the Edge
	 * @param data to append
	 */
	public Vertex(int id, T data) {
		this.id = id;
		this.data = Optional.of(data);
		this.edges = new ArrayList<>();
	}

	/**
	 * Creates a new empty Vertex
	 * 
	 * @param id of the Edge
	 */
	public Vertex(int id) {
		this.id = id;
		this.data = Optional.empty();
		this.edges = new ArrayList<>();
	}

	/**
	 * Appends the Vertex and creates the corresponding Edge
	 * 
	 * @param vertex to append
	 * @return the created Edge
	 */
	public Edge<T> append(Vertex<T> vertex) {
		remove(vertex.getId());
		Edge<T> edge = new Edge<>(this, vertex);
		edges.add(edge);
		return edge;
	}

	/**
	 * Removes the Vertex and its Edge by ID
	 * 
	 * @param id of the Vertex to look for
	 * @return if the Vertex has been removed
	 */
	public boolean remove(int id) {
		return edges.removeIf(edge -> edge.getTo().getId() == id);
	}

	public int getId() {
		return id;
	}

	public boolean hasData() {
		return data.isPresent();
	}

	public T getData() {
		return data.get();
	}

	public void setData(T data) {
		this.data = Optional.of(data);
	}

	public List<Vertex<T>> getAdjacentVertices() {
		return edges.stream().map(edge -> edge.getTo()).collect(Collectors.toList());
	}

	public int getAdjacentVertexCount() {
		return this.edges.size();
	}

	public List<Edge<T>> getEdges() {
		return edges;
	}

	public int getDegree() {
		return edges.size();
	}

	/**
	 * Deep copy Vertex without its Edges
	 * 
	 * @return deep copy of the Vertex
	 */
	public Vertex<T> cloneWithoutEdges() {
		Vertex<T> vertexCopy = new Vertex<>(this.id);
		if (this.hasData())
			vertexCopy.setData(this.getData());
		return vertexCopy;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Vertex<T> other = (Vertex<T>) obj;
		return this.id == other.id;
	}

}
