package graphs;

/**
 * Connection between two Vertices
 *
 * @param <T> type of the Vertices
 */
public class Edge<T> {

	private Vertex<T> from;
	private Vertex<T> to;
	private int weight;

	/**
	 * Creates a new Edge with a standard weight of 1
	 * 
	 * @param from source Vertex of the connection
	 * @param to   destination Vertex of the connection
	 */
	public Edge(Vertex<T> from, Vertex<T> to) {
		this.from = from;
		this.to = to;
		this.weight = 1;
	}

	/**
	 * Creates a new Edge
	 * 
	 * @param from   source Vertex of the connection
	 * @param to     destination Vertex of the connection
	 * @param weight of the Edge
	 */
	public Edge(Vertex<T> from, Vertex<T> to, int weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
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
	 * @return weight of the Edge
	 */
	public int getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + Integer.hashCode(weight);
		return result;
	}

	/**
	 * @return equality of the objects types, source and destination Vertices and
	 *         Edge weights
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
		// Edge weights
		return this.weight == other.weight;
	}

	/**
	 * @return source and destination ids in a string
	 */
	@Override
	public String toString() {
		return String.format("(%2d -- %-2d)", from.getId(), to.getId());
	}

}
