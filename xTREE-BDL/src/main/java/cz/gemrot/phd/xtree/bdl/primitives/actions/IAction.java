package cz.gemrot.phd.xtree.bdl.primitives.actions;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;

public interface IAction<AGENT extends IBDLAgent> {

	public static enum ActionResult {
		
		RUNNING,
		SUCCESS,
		FAIL,
		TERMINATED
		
	}
	
	public ActionResult execute(AGENT agent);
	public ActionResult switchOut(AGENT agent);
	public ActionResult terminate(AGENT agent);
	
}
