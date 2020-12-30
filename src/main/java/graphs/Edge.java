package graphs;

public class Edge<T> {

	private Vertex<T> from;
	private Vertex<T> to;
	private int weight;

	Edge(Vertex<T> from, Vertex<T> to) {
		this.from = from;
		this.to = to;
		this.weight = 1;
	}

	Edge(Vertex<T> from, Vertex<T> to, int weight) {
		this.from = from;
		this.to = to;
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
		@SuppressWarnings("unchecked")
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
		return String.format("(%2d -- %-2d)", from.getId(), to.getId());
	}

}
