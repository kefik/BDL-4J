package cz.gemrot.phd.xtree.bdl.xml;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import cz.gemrot.phd.xtree.generic.xTree;
import junit.framework.Assert;

public class TestLoader {

	@Test
	public void testLoader() {
		
		BDLLoaderXML loader = new BDLLoaderXML();
		
		URL url = getClass().getClassLoader().getResource("cz/gemrot/phd/xtree/bdl/xml/TestTree.xml");
		
		File file = new File(url.getPath());
		
		xTree tree = loader.load(file);
		
		Assert.assertNotNull(tree);		
	}
	
}
