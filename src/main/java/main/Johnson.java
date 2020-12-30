package main;

import java.io.IOException;
import java.util.List;

import graphs.DirectedGraph;

public class Johnson {

	public static void main(String[] args) throws IOException {

		DirectedGraph<Integer> graph = DirectedGraph.fromString("{1,2}\n" +
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

		List<DirectedGraph<Integer>> subGraphs = kAlg.findStronglyConnectedComponents(graph);

		// subGraphs.forEach(System.out::println);

	}

}
