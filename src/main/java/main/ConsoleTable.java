package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConsoleTable {

	public enum Align {

		Left, Right;

		public int value() {
			return this == Align.Left ? -1 : 1;
		}

	}

	private final Align[] alignments;
	private List<String[]> headerRows = new ArrayList<>();
	private List<String[]> bodyRows = new ArrayList<>();
	private int[] colWidths;

	public ConsoleTable(Align... alignments) {
		this.alignments = alignments;
		colWidths = new int[this.alignments.length];
	}

	public void addHeaderRow(String... row) {
		this.addRow(this.headerRows, row);
	}

	public void addBodyRow(String... row) {
		this.addRow(this.bodyRows, row);
	}

	private void addRow(List<String[]> target, String[] row) {
		if (row.length != this.colWidths.length) {
			System.err.println("Number of cells doesn't match the tables number of columns");
			System.err.println(Arrays.toString(row));
			return;
		}
		target.add(row);
		for (int i = 0; i < this.colWidths.length; i++) {
			if (this.colWidths[i] < row[i].length())
				this.colWidths[i] = row[i].length();
		}
	}

	private String generateSeparator() {
		StringBuffer buffer = new StringBuffer();
		for (int width : colWidths)
			buffer.append("+-" + "-".repeat(width) + "-");
		buffer.append("+");
		return buffer.toString();
	}

	private String generateFormat() {
		return generateFormat(Optional.empty());
	}

	private String generateFormat(Align forcedAlign) {
		return generateFormat(Optional.of(forcedAlign));
	}

	private String generateFormat(Optional<Align> optForcedAlign) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < this.colWidths.length; i++) {
			Align alignment = optForcedAlign.orElse(this.alignments[i]);
			int stringModifier = alignment.value();
			buffer.append("| %" + (this.colWidths[i] * stringModifier) + "s ");
		}
		buffer.append("|\n");
		return buffer.toString();
	}

	@Override
	public String toString() {

		String separator = generateSeparator();
		String headerFormat = generateFormat(Align.Left);
		String bodyFormat = generateFormat();

		StringBuffer result = new StringBuffer();
		result.append(separator + "\n");

		// append header
		for (String[] row : this.headerRows)
			result.append(String.format(headerFormat, (Object[]) row));

		// separate header and body if both exist
		if (this.headerRows.size() > 0 && this.bodyRows.size() > 0)
			result.append(separator + "\n");

		// append body
		for (String[] row : this.bodyRows)
			result.append(String.format(bodyFormat, (Object[]) row));

		result.append(separator);

		return result.toString();

	}

	public void display() {
		System.out.println(this.toString());
	}

}
