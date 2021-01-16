package main;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import algo.Johnson;
import graphs.DirectedGraph;
import graphs.Vertex;

public class Analyzer {

	static final String filename = "qube.txt";
	static final String basepath = "testGraphs/";

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

		// set headers
		table.addHeaderRow("Graph", filename);
		table.addHeaderRow("Cycles", Integer.toString(cycles.size()));

		// create number string formats
		final String cycleIndexFormat = getUniformIntFormat(cycles.size() - 1);
		final String cycleLengthFormat = getUniformIntFormat(cycles.stream()
				.mapToInt(c -> c.getVertexCount()));
		final String vertexIndexFormat = getUniformIntFormat(cycles.stream()
				.map(c -> c.getAllVertices())
				.flatMap(vs -> vs.stream())
				.mapToInt(v -> v.getId()));
		final String cycleHeaderFormat = "Cycle " + cycleIndexFormat + " (length " + cycleLengthFormat + ")";

		// fill table
		for (int i = 0; i < cycles.size(); i++) {
			DirectedGraph<Integer> cycle = cycles.get(i);
			String name = String.format(cycleHeaderFormat, i + 1, cycle.getVertexCount());
			String value = graphToCycleString(cycle, vertexIndexFormat);
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
	public static String graphToCycleString(DirectedGraph<Integer> cycle, String vertexIndexFormat)
			throws IndexOutOfBoundsException {

		StringBuffer buffer = new StringBuffer();

		final Vertex<Integer> startVertex = cycle.getAllVertices().get(0);
		Vertex<Integer> currentVertex = startVertex;

		do {
			buffer.append(String.format(vertexIndexFormat + " -> ", currentVertex.getId()));
			currentVertex = currentVertex.getAdjacentVertices().get(0);
		} while (!currentVertex.equals(startVertex));

		buffer.append(String.format(vertexIndexFormat, startVertex.getId()));

		return buffer.toString();
	}

	/**
	 * Creates a uniform format, which fits all input numbers to display them with
	 * equal width<br>
	 * "%(+)0&lt;n&gt;d"
	 * 
	 * @param integers to consider
	 * @return format
	 */
	public static String getUniformIntFormat(int... integers) {
		return getUniformIntFormat(IntStream.of(integers));
	}

	/**
	 * Creates a uniform format, which fits all input numbers to display them with
	 * equal width<br>
	 * "%(+)0&lt;n&gt;d"
	 * 
	 * @param integers to consider
	 * @return format
	 */
	public static String getUniformIntFormat(IntStream integers) {
		int maxNumber = 1;
		boolean containsNegativeNumbers = false;
		for (Iterator<Integer> iterator = integers.iterator(); iterator.hasNext();) {
			int i = (int) iterator.next();
			if (i > maxNumber || -i > maxNumber)
				maxNumber = Math.abs(i);
			if (i < 0)
				containsNegativeNumbers = true;
		}
		final int maxDigitCount = (int) Math.log10(maxNumber) + 1;
		return (containsNegativeNumbers ? "%+0" : "%0") +
				(containsNegativeNumbers ? maxDigitCount + 1 : maxDigitCount) +
				"d";
	}

}
