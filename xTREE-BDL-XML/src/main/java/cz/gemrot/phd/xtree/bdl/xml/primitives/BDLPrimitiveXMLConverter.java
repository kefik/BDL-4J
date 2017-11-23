package cz.gemrot.phd.xtree.bdl.xml.primitives;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BDLPrimitiveXMLConverter implements Converter {

	public static final BDLPrimitiveXMLConverter INSTANCE = new BDLPrimitiveXMLConverter();
	
    public boolean canConvert(Class clazz) {
        return clazz.equals(BDLPrimitiveXML.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer,  MarshallingContext context) {
    	// not supported...
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        BDLPrimitiveXML result = new BDLPrimitiveXML();
        
        result.primitive = reader.getNodeName();
        
        while (reader.hasMoreChildren()) {
    		// READ PARAM NAME
    		reader.moveDown();
    		String name = reader.getNodeName();
    		
    		// READ PARAM VALUE
    		Object value = null;
    		reader.moveDown();    		
    		if ("cnd".equals(reader.getNodeName().toLowerCase())) {
    			BDLConditionXML cnd = new BDLConditionXML();
    			value = context.convertAnother(cnd, BDLConditionXML.class, BDLConditionXMLConverter.INSTANCE);    			
    		} else
    		if ("action".equals(reader.getNodeName().toLowerCase())){
    			BDLActionXML action = new BDLActionXML();
    			value = context.convertAnother(action, BDLActionXML.class, BDLActionXMLConverter.INSTANCE);
    		} else {
    			BDLPrimitiveXML primitive = new BDLPrimitiveXML();
    			value = context.convertAnother(primitive, BDLPrimitiveXML.class, BDLPrimitiveXMLConverter.INSTANCE);
    		}    		
    		reader.moveUp();
    		reader.moveUp();
    		
    		// SAVE PARAM
    		result.children.add(new Tuple2<String, Object>(name, value));
    	}
        
        return result;
    }

}