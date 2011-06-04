package fitedit.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitedit.Constants;

public class FitUtilTest {
	private FitUtil util;

	@Before
	public void setUp() throws Exception {
		util = new FitUtil();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFitnesseUrl() {
		String root = Constants.FITNESSE_ROOT;

		Assert.assertEquals("AAA.BBB",
				FitUtil.getFitnesseUrl(root + "/AAA/BBB"));
		Assert.assertEquals("AAA.BBB",
				FitUtil.getFitnesseUrl(root + "/AAA/BBB/"));
		Assert.assertEquals("AAA.BBB",
				FitUtil.getFitnesseUrl(root + "/AAA/BBB/content.txt"));

		Assert.assertEquals(
				"AAA.BBB",
				FitUtil.getFitnesseUrl("/XXX/YYY/" + root
						+ "/AAA/BBB/content.txt"));

		Assert.assertEquals(
				"AAA.BBB",
				FitUtil.getFitnesseUrl("XXX/YYY/" + root
						+ "/AAA/BBB/content.txt"));

		Assert.assertEquals("", FitUtil.getFitnesseUrl(root));
		Assert.assertEquals("", FitUtil.getFitnesseUrl("/" + root + "/"));
		Assert.assertEquals("", FitUtil.getFitnesseUrl(""));
		Assert.assertEquals("", FitUtil.getFitnesseUrl("///"));
		Assert.assertNull(FitUtil.getFitnesseUrl(null));
	}
}
