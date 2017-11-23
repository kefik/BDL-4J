package cz.gemrot.phd.xtree.bdl.xml.primitives;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BDLConditionXMLConverter implements Converter {

	public static final BDLConditionXMLConverter INSTANCE = new BDLConditionXMLConverter();
	
    public boolean canConvert(Class clazz) {
        return clazz.equals(BDLConditionXML.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer,  MarshallingContext context) {
    	// not supported...
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    	BDLConditionXML result = new BDLConditionXML();
        
    	result.condition = reader.getValue();
    	
    	return result;
    }

}