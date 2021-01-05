package main;

import java.util.List;

import graphs.DirectedGraph;

public interface Johnson<T> {

	public List<DirectedGraph<T>> getCycles(DirectedGraph<T> graph);

}
