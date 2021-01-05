package main;

public class JohnsonRecursiveTest  extends JohnsonTestBase<JohnsonRecursive<Integer>> {

	@Override
	protected JohnsonRecursive<Integer> createJohnsonInstance() {
		return new JohnsonRecursive<>();
	}

}
