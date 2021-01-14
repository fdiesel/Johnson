package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleTable {
	
	public enum Align {
		Left, Right;
		public int value() {
			if (this == Align.Left) return -1;
			else return 1;
		}
	}

	private Align[] alignments;
	private List<String[]> titleRows = new ArrayList<>();
	private List<String[]> bodyRows = new ArrayList<>();
	private int[] colWidths;
	
	public ConsoleTable(Align... alignments) {
		this.alignments = alignments;
		colWidths = new int[this.alignments.length];
	}
	
	public void addTitleRow(String... row) {
		this.addRow(this.titleRows, row);
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
			if (this.colWidths[i] < row[i].length()) this.colWidths[i] = row[i].length();
		}
	}
	
	@Override
	public String toString() {
		String spacer = "";
		String titleFormat = "";
		String bodyFormat = "";
		for (int i = 0; i < this.colWidths.length; i++) {
			spacer += "+-" + "-".repeat(this.colWidths[i]) + "-";
			titleFormat += "| %-" + this.colWidths[i] + "s ";
			bodyFormat += "| %" + (this.colWidths[i] * this.alignments[i].value()) + "s ";
		}
		spacer += "+";
		titleFormat += "|\n";
		bodyFormat += "|\n";
		String out = spacer + "\n";
		for (String[] row : this.titleRows) out += String.format(titleFormat, (Object[]) row);
		if (this.titleRows.size() > 0 && this.bodyRows.size() > 0) out += spacer + "\n";
		for (String[] row : this.bodyRows) out += String.format(bodyFormat, (Object[]) row);
		out += spacer;
		return out;
	}
	
	public void println() {
		System.out.println(this.toString());
	}

}
