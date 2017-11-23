package cz.gemrot.phd.xtree.bdl.signals;

import cz.gemrot.phd.utils.sockets.ConfigMap;

/**
 * Marks "RESULT" signals that points broadcast once they "finish".
 * 
 * @author Jakub Gemrot
 */
public class BDLxSignalFinished extends BDLxSignalResult {

	public BDLxSignalFinished(String name) {
		super(name, false, null);
	}
	
	public BDLxSignalFinished(String name, ConfigMap userArgs) {
		super(name, false, userArgs);
	}
	
	public BDLxSignalFinished(String name, boolean done, ConfigMap userArgs) {
		super(name, done, userArgs);
	}
	
	@Override
	public String toString() {
		return "BDLxSignalFinished[" + getName() + ",planCounter=" + getPlanCounter() + ",recursionCounter=" + getRecursionCounter() + "]";
	}
	
}