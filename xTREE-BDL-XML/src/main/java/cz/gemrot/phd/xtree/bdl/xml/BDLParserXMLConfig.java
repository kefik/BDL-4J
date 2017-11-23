package cz.gemrot.phd.xtree.bdl.xml;

import com.thoughtworks.xstream.XStream;

import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLActionXML;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLActionXMLConverter;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLConditionXML;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLConditionXMLConverter;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLPrimitiveXML;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLPrimitiveXMLConverter;

public class BDLParserXMLConfig {

	public void configXStream(XStream xstream) {
		xstream.alias("body", BDLPrimitiveXML.class);
		xstream.alias("switch", BDLPrimitiveXML.class);
		xstream.alias("guard", BDLPrimitiveXML.class);
		xstream.alias("cnd", BDLConditionXML.class);
		xstream.alias("action", BDLActionXML.class);		
		
		xstream.registerConverter(BDLPrimitiveXMLConverter.INSTANCE);
		xstream.registerConverter(BDLConditionXMLConverter.INSTANCE);
		xstream.registerConverter(BDLActionXMLConverter.INSTANCE);
	}
	
}
