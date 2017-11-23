package cz.gemrot.phd.xtree.bdl.signals;

import cz.gemrot.phd.utils.sockets.ConfigMap;

/**
 * Marks "RESULT" signals.
 * 
 * @author Jakub Gemrot
 */
public class BDLxSignalResult extends BDLxSignal {

	public BDLxSignalResult(String name) {
		super(name, false, null);
	}
	
	public BDLxSignalResult(String name, ConfigMap userArgs) {
		super(name, false, userArgs);
	}
	
	public BDLxSignalResult(String name, boolean done, ConfigMap userArgs) {
		super(name, done, userArgs);
	}
	
	@Override
	public String toString() {
		return "BDLxSignalResult[" + getName() + ",planCounter=" + getPlanCounter() + ",recursionCounter=" + getRecursionCounter() + "]";
	}
		
}