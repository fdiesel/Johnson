package graphs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class VertexTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {}

	@Test
	void testAppend() {
		Vertex<Integer> from = new Vertex<>(0);
		Vertex<Integer> to1 = new Vertex<>(1);
		Vertex<Integer> to2 = new Vertex<>(2);
		from.append(to1);
		from.append(to2);
		assertEquals(2, from.getAdjacentVertexCount());
		assertEquals(0, to1.getAdjacentVertexCount());
		assertEquals(0, to2.getAdjacentVertexCount());
	}

	@Test
	void testCreatedEdge() {
		Vertex<Integer> from = new Vertex<>(0);
		Vertex<Integer> to = new Vertex<>(1);
		Edge<Integer> createdEdge = from.append(to);
		assertEquals(from, createdEdge.getFrom());
		assertEquals(to, createdEdge.getTo());
	}

	@Test
	void testRemove() {
		Vertex<Integer> from = new Vertex<>(0);
		Vertex<Integer> to = new Vertex<>(1);
		from.append(to);
		from.remove(to.getId());
		assertEquals(0, from.getAdjacentVertexCount());
		assertEquals(0, to.getAdjacentVertexCount());
	}

	@Test
	void testEqualsObjectSuccess() {
		Vertex<String> vertex1 = new Vertex<>(0, "Something");
		Vertex<String> vertex2 = new Vertex<>(0, "Else");
		assertTrue(vertex1.equals(vertex2));
	}

	@Test
	void testEqualsObjectFailure() {
		Vertex<String> vertex1 = new Vertex<>(0, "Something");
		Vertex<String> vertex2 = new Vertex<>(1, "Else");
		assertFalse(vertex1.equals(vertex2));
	}

}
