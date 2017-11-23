package cz.gemrot.phd.xtree.bdl.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.gemrot.phd.xtree.bdl.primitives.BDLPrimitive;
import cz.gemrot.phd.xtree.bdl.primitives.actions.SpelAction;
import cz.gemrot.phd.xtree.bdl.primitives.cores.BDLCoreAction;
import cz.gemrot.phd.xtree.bdl.primitives.cores.BDLCoreBody;
import cz.gemrot.phd.xtree.bdl.primitives.cores.BDLCoreGuard;
import cz.gemrot.phd.xtree.bdl.primitives.cores.BDLCoreRoot;
import cz.gemrot.phd.xtree.bdl.primitives.cores.BDLCoreSwitch;
import cz.gemrot.phd.xtree.bdl.primitives.sensors.ICondition;
import cz.gemrot.phd.xtree.bdl.primitives.sensors.SpelCondition;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLActionXML;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLConditionXML;
import cz.gemrot.phd.xtree.bdl.xml.primitives.BDLPrimitiveXML;
import cz.gemrot.phd.xtree.bdl.xml.primitives.Tuple2;
import cz.gemrot.phd.xtree.generic.xTree;

public class BDLLoaderXML {

	private BDLParserXMLConfig parserConfig;
	private BDLParserXML parser;

	public BDLLoaderXML() {
		parserConfig = new BDLParserXMLConfig();
		parser = new BDLParserXML(parserConfig);
	}
	
	public xTree load(File xmlFile) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(xmlFile);
			return load(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load BDL plan from: " + xmlFile, e);
		} finally {
			if (inputStream != null) {
				try { inputStream.close(); } catch (Exception e) {}
			}
		}		
	}
	
	public xTree load(InputStream xmlStream) {
		BDLPrimitiveXML rootXML = parser.load(xmlStream);
		
		BDLPrimitive rootPrimitive = makePrimitive(rootXML);
		
		BDLPrimitive trueRoot = new BDLPrimitive(new BDLCoreRoot(rootPrimitive));
		
		xTree result = new xTree(trueRoot, BDLxSignal.EXECUTE);
		
		return result;
	}
	
	protected Object make(Object xml) {
		if (xml instanceof BDLConditionXML) return makeCondition((BDLConditionXML) xml);
		if (xml instanceof BDLActionXML) return makeAction((BDLActionXML) xml);
		if (xml instanceof BDLPrimitiveXML) return makePrimitive((BDLPrimitiveXML) xml);
		throw new RuntimeException("Unhandled xml class: " + xml.getClass());
	}
	
	protected ICondition makeCondition(BDLConditionXML xml) {
		return new SpelCondition(xml.condition);
	}
	
	protected BDLPrimitive makeAction(BDLActionXML xml) {
		return new BDLPrimitive(new BDLCoreAction(new SpelAction(xml.action)));
	}
	
	protected BDLPrimitive makePrimitive(BDLPrimitiveXML xml) {
		xml.primitive = xml.primitive.toLowerCase();
		
		if (xml.primitive.equals("switch")) return makeSwitch(xml);
		if (xml.primitive.equals("guard")) return makeGuard(xml);
		if (xml.primitive.equals("body")) return makeBody(xml);
		
		throw new RuntimeException("Unsupprted primitive: " + xml.primitive);
	}

	private BDLPrimitive makeSwitch(BDLPrimitiveXML xml) {
		ICondition cnd = null;
		BDLPrimitive thenPoint = null;
		BDLPrimitive elsePoint = null;
		
		for (Tuple2<String, Object> element : xml.children) {
			element.a = element.a.toLowerCase();
			if (element.a.equals("cnd")) {
				cnd = makeCondition((BDLConditionXML)element.b);
			} else
			if (element.a.equals("then")) {
				thenPoint = (BDLPrimitive) make(element.b);
			} else
			if (element.a.equals("else")) {
				elsePoint = (BDLPrimitive) make(element.b);
			}
		}
		
		return new BDLPrimitive(new BDLCoreSwitch(cnd, thenPoint, elsePoint));
	}
	
	private BDLPrimitive makeGuard(BDLPrimitiveXML xml) {
		ICondition cnd = null;
		BDLPrimitive childPoint = null;
		
		for (Tuple2<String, Object> element : xml.children) {
			element.a = element.a.toLowerCase();
			if (element.a.equals("cnd")) {
				cnd = makeCondition((BDLConditionXML)element.b);
			} else
			if (element.a.equals("child")) {
				childPoint = (BDLPrimitive) make(element.b);
			}
		}
		
		return new BDLPrimitive(new BDLCoreGuard(cnd, childPoint));
	}

	private BDLPrimitive makeBody(BDLPrimitiveXML xml) {
		List<BDLPrimitive> primitives = new ArrayList<BDLPrimitive>(xml.children.size());
		for (Tuple2<String, Object> element : xml.children) {
			primitives.add((BDLPrimitive)make(element.b));
		}
		return new BDLPrimitive(new BDLCoreBody(primitives));
	}
	
}
