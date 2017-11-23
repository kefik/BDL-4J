package cz.gemrot.phd.xtree.bdl.xml.primitives;

import java.util.ArrayList;
import java.util.List;

public class BDLPrimitiveXML {

	public String primitive;
	
	/**
	 * List of: PARAM NAME -> PARAM VALUE
	 */
	public List<Tuple2<String, Object>> children = new ArrayList<Tuple2<String, Object>>();
	
}
