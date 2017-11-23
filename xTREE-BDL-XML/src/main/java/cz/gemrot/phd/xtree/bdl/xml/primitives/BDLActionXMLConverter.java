package cz.gemrot.phd.xtree.bdl.xml.primitives;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BDLActionXMLConverter implements Converter {

	public static final BDLActionXMLConverter INSTANCE = new BDLActionXMLConverter();
	
    public boolean canConvert(Class clazz) {
        return clazz.equals(BDLActionXML.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer,  MarshallingContext context) {
    	// not supported...
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    	BDLActionXML result = new BDLActionXML();
        
    	result.action = reader.getValue();
    	
    	return result;
    }

}