package main;

import graphs.Graph;

public class Analyzer {

	static final String filename = "data.txt";

	public static void main(String[] args) throws Exception {

		Graph<Integer> graph = Graph.fromFile(filename);

		System.out.println(graph);

	}

}
