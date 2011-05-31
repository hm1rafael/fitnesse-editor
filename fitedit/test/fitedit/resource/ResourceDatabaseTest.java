package fitedit.resource;

import org.junit.Test;


public class ResourceDatabaseTest {

	@Test
	public void testSelect(){
		ResourceDatabase.getInstance().init();
		ResourceDatabase.getInstance().add("aaa", "bbbb");
		System.out.println(ResourceDatabase.getInstance().get("aaa"));
	}
	
}
