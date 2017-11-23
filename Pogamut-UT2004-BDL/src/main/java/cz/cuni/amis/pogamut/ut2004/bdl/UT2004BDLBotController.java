package cz.cuni.amis.pogamut.ut2004.bdl;

import java.io.InputStream;
import java.util.logging.Logger;

import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.utils.exception.PogamutException;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.xml.BDLLoaderXML;
import cz.gemrot.phd.xtree.generic.xTree;
import cz.gemrot.phd.xtree.generic.agent.IBodyActions;
import cz.gemrot.phd.xtree.generic.agent.IDetermination;
import cz.gemrot.phd.xtree.generic.agent.IMemory;
import cz.gemrot.phd.xtree.generic.agent.IPercepts;
import cz.gemrot.phd.xtree.generic.agent.IReasoningActions;

@SuppressWarnings("rawtypes")
@AgentScoped
public abstract class UT2004BDLBotController<BOT extends UT2004Bot> extends UT2004BotModuleController<BOT> implements IBodyActions, IReasoningActions {

	private class BDLAgentAdapter implements IBDLAgent {

		@Override
		public IPercepts getPercepts() {
			return null;
		}

		@Override
		public IMemory getMemory() {
			return null;
		}

		@Override
		public IDetermination getDetermination() {
			return null;
		}

		@Override
		public IBodyActions getBody() {
			return UT2004BDLBotController.this;
		}

		@Override
		public IReasoningActions getReasoning() {
			return UT2004BDLBotController.this;
		}

		@Override
		public Logger getLog() {
			return UT2004BDLBotController.this.getLog();
		}
		
	}
	
	protected IBDLAgent agentAdapter = new BDLAgentAdapter();
	
	protected xTree xtree;
	
	protected InputStream getResourceStream(String resource) {		
		log.info("Looking for resource at: " + resource);
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
		
		if (stream == null) {
			throw new RuntimeException("Failed to get resource stream from: " + resource);
		}
		
		log.info("Resource found.");
		
		return stream;
	}
	
	public abstract InputStream getBDLPlanXMLStream();
	
	public void finishControllerInitialization() {    	
		super.finishControllerInitialization();
		
		log.info("Getting BDL plan XML stream...");
		
		InputStream xmlStream = getBDLPlanXMLStream();
		
		log.fine("Getting BDL plan XML stream obtained.");
		
		BDLLoaderXML loaderXML = new BDLLoaderXML();
		
		log.info("Loading BDL plan from XML stream...");
		
		xtree = loaderXML.load(xmlStream);
		
		try { xmlStream.close(); } catch (Exception e) {}
		
		if (xtree == null) {
			log.severe("BDL plan load failed! xtree is null!");
			throw new RuntimeException("Failed to load BDL plan!");
		}
		
		log.info("BDL plan loaded.");
	}
	
	
    private int logicIterationNumber = 0;
   
    private long logicLastMillis = -1;

    @SuppressWarnings("unchecked")
	@Override
    public void logic() throws PogamutException {
    	if (logicLastMillis < 0) {
    		logicLastMillis = System.currentTimeMillis();
    	}
    	
    	long timeDelta = System.currentTimeMillis() - logicLastMillis;
    	logicLastMillis = System.currentTimeMillis();
    	
    	log.info("---LOGIC[" + (++logicIterationNumber) + " | timeDelta = " + timeDelta + "ms]---");
    	xtree.run(agentAdapter);    	
    	log.info("xTree Time: " + (System.currentTimeMillis() - logicLastMillis) + "ms");
    }

}
