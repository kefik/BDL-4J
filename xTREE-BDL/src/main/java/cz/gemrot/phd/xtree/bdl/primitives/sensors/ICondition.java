package cz.gemrot.phd.xtree.bdl.primitives.sensors;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;

public interface ICondition<AGENT extends IBDLAgent> {

	public boolean evaluate(AGENT agent);
	
}
