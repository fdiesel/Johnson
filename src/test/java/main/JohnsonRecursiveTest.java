package main;

import algo.Johnson;

public class JohnsonRecursiveTest extends JohnsonTestBase<Johnson<Integer>> {

	@Override
	protected Johnson<Integer> createJohnsonInstance() {
		return new Johnson<>();
	}

}
