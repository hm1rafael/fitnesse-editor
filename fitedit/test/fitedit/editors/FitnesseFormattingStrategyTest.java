package fitedit.editors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FitnesseFormattingStrategyTest {

	private FitnesseFormattingStrategy strategy;

	@Before
	public void setUp() throws Exception {
		strategy = new FitnesseFormattingStrategy();
	}

	@Test
	public void testFormat() {
		String result = strategy.fitnesseFormat("aaa");
		Assert.assertEquals("aaa", result);

		result = strategy.fitnesseFormat("|aaa|bbbbbbb|\n|xxx|yyyy|");
		Assert.assertEquals("|aaa|bbbbbbb|\n|xxx|yyyy   |", result);
	}

}
