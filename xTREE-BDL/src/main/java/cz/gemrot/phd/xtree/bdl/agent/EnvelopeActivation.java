package cz.gemrot.phd.xtree.bdl.agent;

import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointLayer;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint;

public class EnvelopeActivation {
	
	private BDLPoint caller;
	
	private int planCounter;
	
	private int resultPlanCounter;
	
	private BDLPointLayer shell;
		
	public EnvelopeActivation(BDLPoint caller, int planCounter, int resultPlanCounter, BDLPointLayer shell) {
		super();
		this.shell = shell;		
		this.caller = caller;
		
		this.planCounter = planCounter;
		this.resultPlanCounter = resultPlanCounter;
	}
	
	public BDLPointLayer getShell() {
		return shell;
	}

	public int getPlanCounter() {
		return planCounter;
	}
	
	public BDLPoint getCaller() {
		return caller;
	}

	public int getResultPlanCounter() {
		return resultPlanCounter;
	}
	
}
