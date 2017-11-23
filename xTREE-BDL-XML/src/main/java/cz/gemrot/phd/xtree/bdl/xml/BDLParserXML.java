package cz.gemrot.phd.xtree.bdl.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLPrimitiveXML;

public class BDLParserXML {

	protected XStream xstream;
	
	public BDLParserXML(BDLParserXMLConfig config) {
		xstream = init(config);		
	}
	
	protected XStream init(BDLParserXMLConfig config) {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		config.configXStream(xstream);
		return xstream;

	}
	public BDLPrimitiveXML load(File xmlFile) {
		if (xmlFile == null) {
			throw new IllegalArgumentException("'xmlFile' can't be null!");
		}
		try {
			return load(new FileInputStream(xmlFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not load file: " + xmlFile.getAbsolutePath(), e);
		}
	}
	
	public BDLPrimitiveXML load(InputStream xmlStream) {
		if (xmlStream == null) {
			throw new IllegalArgumentException("'xmlStream' can't be null!");
		}		
		
		Object obj = xstream.fromXML(xmlStream);
		try {
			xmlStream.close();
		} catch (IOException e) {
		}
		if (obj == null || !BDLPrimitiveXML.class.isAssignableFrom(obj.getClass())) {
			throw new RuntimeException("Stream didn't contain an xml with " + BDLPrimitiveXML.class.getSimpleName());
		}
		
		@SuppressWarnings("unchecked")
		BDLPrimitiveXML result = (BDLPrimitiveXML)obj;
		
		return result;
	}
	
}
