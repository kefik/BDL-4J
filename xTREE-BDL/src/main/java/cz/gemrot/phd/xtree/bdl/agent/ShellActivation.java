package cz.gemrot.phd.xtree.bdl.agent;

import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointLayer;

public class ShellActivation {
	
	private BDLPoint caller;
	
	private int recursion;
	
	private int resultRecursion;
	
	private BDLPointLayer nut;
	
	public ShellActivation(BDLPoint caller, int recursion, int resultRecursion, BDLPointLayer nut) {
		super();
		this.nut = nut;		
		this.recursion = recursion;
		this.caller = caller;
		this.resultRecursion = resultRecursion;
	}
	
	public BDLPointLayer getNut() {
		return nut;
	}

	public int getRecursion() {
		return recursion;
	}
	
	public BDLPoint getCaller() {
		return caller;
	}

	public int getResultRecursion() {
		return resultRecursion;
	}
	
}
