package main;

import java.util.List;

import algo.Johnson;
import graphs.DirectedGraph;
import graphs.Vertex;

public class Analyzer {

	static final String filename = "qube.txt";
	static final String basepath = "testGraphs/";
	static final String numberFormat = "%02d";

	/**
	 * Find simple cycles in a Graph and display them
	 * 
	 * @param args
	 * @throws Exception if the file can't be found or read
	 */
	public static void main(String[] args) throws Exception {

		DirectedGraph<Integer> graph = DirectedGraph.fromFile(basepath + filename);

		Johnson<Integer> johnson = new Johnson<>();

		List<DirectedGraph<Integer>> cycles = johnson.getSimpleCycles(graph);

		displayCycles(cycles);

	}

	/**
	 * Pretty print a list of Cycles (Graphs)
	 * 
	 * @param cycles
	 */
	public static void displayCycles(List<DirectedGraph<Integer>> cycles) {

		ConsoleTable table = new ConsoleTable(ConsoleTable.Align.Left, ConsoleTable.Align.Left);

		table.addHeaderRow("Graph", filename);
		table.addHeaderRow("Cycles", Integer.toString(cycles.size()));

		final int maxCycleDigitCount = (int) Math.log10(cycles.size() + 1) + 1;
		final String cycleHeaderFormat = "Cycle %0" + maxCycleDigitCount + "d (length " + numberFormat + ")";

		for (int i = 0; i < cycles.size(); i++) {

			DirectedGraph<Integer> cycle = cycles.get(i);

			String name = String.format(cycleHeaderFormat, i + 1, cycle.getVertexCount());
			String value = graphToCycleString(cycle);

			table.addBodyRow(name, value);

		}

		table.display();
	}

	/**
	 * Converts a Graph, which consists of a Cycle into a String separated by
	 * arrows
	 * 
	 * @param cycle Graph to read
	 * @return 01 -> 02 -> ...
	 * @throws IndexOutOfBoundsException if the Graph doesn't or doesn't only
	 *                                   consist of a Cycle
	 */
	public static String graphToCycleString(DirectedGraph<Integer> cycle) throws IndexOutOfBoundsException {

		StringBuffer buffer = new StringBuffer();

		final Vertex<Integer> startVertex = cycle.getAllVertices().get(0);
		Vertex<Integer> currentVertex = startVertex;

		do {
			buffer.append(String.format(numberFormat + " -> ", currentVertex.getId()));
			currentVertex = currentVertex.getAdjacentVertices().get(0);
		} while (!currentVertex.equals(startVertex));

		buffer.append(String.format(numberFormat, startVertex.getId()));

		return buffer.toString();
	}

}
