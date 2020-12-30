package main;

import graphs.DirectedGraph;

public class Analyzer {

	static final String filename = "data.txt";

	public static void main(String[] args) throws Exception {

		DirectedGraph<Integer> graph = DirectedGraph.fromFile(filename);

		System.out.println(graph);

	}

}
