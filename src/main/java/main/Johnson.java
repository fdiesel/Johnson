package de.floriandiesel.Johnson;

import java.io.IOException;
import java.util.List;

import graphs.Graph;

public class Johnson {

	public static void main(String[] args) throws IOException {

		Graph<Integer> graph = Graph.fromString("{1,2}\n" +
				"{1,8}\n" +
				"{2,3}\n" +
				"{2,7}\n" +
				"{2,0}\n" +
				"{3,2}\n" +
				"{3,1}\n" +
				"{3,4}\n" +
				"{3,6}\n" +
				"{4,5}\n" +
				"{5,2}\n" +
				"{6,4}\n" +
				"{8,0}\n" +
				"{0,8}");

		// System.out.println(graph);

		KosarajusAlgorithm kAlg = new KosarajusAlgorithm();

		List<Graph<Integer>> subGraphs = kAlg.findStronglyConnectedComponents(graph);

		// subGraphs.forEach(System.out::println);

	}

}
