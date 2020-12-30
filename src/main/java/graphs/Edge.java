package graphs;

/**
 * Connection between two Vertices
 *
 * @param <T> type of the Vertices
 */
public class Edge<T> {

	private Vertex<T> from;
	private Vertex<T> to;

	/**
	 * Creates a new Edge
	 * 
	 * @param from source Vertex of the connection
	 * @param to   destination Vertex of the connection
	 */
	public Edge(Vertex<T> from, Vertex<T> to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * @return source Vertex
	 */
	public Vertex<T> getFrom() {
		return from;
	}

	/**
	 * @return destination Vertex
	 */
	public Vertex<T> getTo() {
		return to;
	}

	/**
	 * @return hashCode, including the Vertices
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/**
	 * @return true on equal types, Vertices (ids)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Edge<T> other = (Edge<T>) obj;
		// source Vertices
		if (from == null && other.from != null) {
			return false;
		} else if (!from.equals(other.from)) {
			return false;
		}
		// destination Vertices
		if (to == null && other.to != null) {
			return false;
		} else if (!to.equals(other.to)) {
			return false;
		}
		return true;
	}

	/**
	 * @return source and destination ids in a String
	 */
	@Override
	public String toString() {
		return String.format("(%2d -- %-2d)", from.getId(), to.getId());
	}

}
