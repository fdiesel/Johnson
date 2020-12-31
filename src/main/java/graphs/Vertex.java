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
		Edge<T> edge = new Edge<>(this, vertex);
		edges.add(edge);
		return edge;
	}

	/**
	 * Removes the Vertex and its Edge by ID
	 * 
	 * @param vertexId to look for
	 * @return if the Vertex has been removed
	 */
	public boolean remove(int vertexId) {
		return edges.removeIf(edge -> edge.getTo().getId() == vertexId);
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

	@Override
	public String toString() {
		return String.valueOf(id);
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
		@SuppressWarnings("unchecked")
		Vertex<T> other = (Vertex<T>) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
