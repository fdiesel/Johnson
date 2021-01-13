package main;

import algo.Johnson;
import graphs.DirectedGraph;

public class Analyzer {

	static final String filename = "testGraphs/qube.txt";

	public static void main(String[] args) throws Exception {

		DirectedGraph<Integer> graph = DirectedGraph.fromFile(filename);

		Johnson<Integer> johnson = new Johnson<>();

		johnson.getCycles(graph).forEach(System.out::println);

	}

}
