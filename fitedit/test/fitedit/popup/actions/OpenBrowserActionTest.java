package fitedit.popup.actions;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitedit.Constants;

public class OpenBrowserActionTest {

	private OpenBrowserAction action;

	@Before
	public void setUp() throws Exception {
		action = new OpenBrowserAction();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFitnesseUrl() {
		String root = Constants.FITNESSE_ROOT;

		Assert.assertEquals("AAA.BBB", action.getFitnesseUrl(root + "/AAA/BBB"));
		Assert.assertEquals("AAA.BBB",
				action.getFitnesseUrl(root + "/AAA/BBB/"));
		Assert.assertEquals("AAA.BBB",
				action.getFitnesseUrl(root + "/AAA/BBB/content.txt"));

		Assert.assertEquals(
				"AAA.BBB",
				action.getFitnesseUrl("/XXX/YYY/" + root
						+ "/AAA/BBB/content.txt"));

		Assert.assertEquals(
				"AAA.BBB",
				action.getFitnesseUrl("XXX/YYY/" + root
						+ "/AAA/BBB/content.txt"));

		Assert.assertEquals("", action.getFitnesseUrl(root));
		Assert.assertEquals("", action.getFitnesseUrl("/" + root + "/"));
		Assert.assertEquals("", action.getFitnesseUrl(""));
		Assert.assertEquals("", action.getFitnesseUrl("///"));
		Assert.assertNull(action.getFitnesseUrl(null));
	}
}
