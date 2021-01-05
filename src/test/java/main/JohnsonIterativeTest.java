package main;

public class JohnsonIterativeTest extends JohnsonTestBase<JohnsonIterative<Integer>> {

	@Override
	protected JohnsonIterative<Integer> createJohnsonInstance() {
		return new JohnsonIterative<>();
	}
}
