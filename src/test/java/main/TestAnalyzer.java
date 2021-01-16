package main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAnalyzer {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {}

	@BeforeEach
	void setUp() throws Exception {}

	@Test
	void testGetUniformIntFormatIntArraySimple() {
		String format = Analyzer.getUniformIntFormat(2, 10);
		String actual = String.format(format, 2);
		assertEquals("02", actual);
	}

	@Test
	void testGetUniformIntFormatIntArrayNegative() {
		String format = Analyzer.getUniformIntFormat(2, -999);
		String actual = String.format(format, -999);
		assertEquals("-999", actual);
	}

	@Test
	void testGetUniformIntFormatIntArrayPositive() {
		String format = Analyzer.getUniformIntFormat(-999, 2);
		String actual = String.format(format, 999);
		assertEquals("+999", actual);
	}

}
