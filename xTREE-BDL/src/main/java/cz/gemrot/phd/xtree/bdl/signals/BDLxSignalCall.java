package cz.gemrot.phd.xtree.bdl.signals;

import cz.gemrot.phd.utils.sockets.ConfigMap;

/**
 * Marks "CALL" signals.
 * 
 * @author Jakub Gemrot
 */
public class BDLxSignalCall extends BDLxSignal {

	public BDLxSignalCall(String name, ConfigMap userArgs) {
		super(name, userArgs);
	}
	
	@Override
	public String toString() {
		return "BDLxSignalCall[" + getName() + ",planCounter=" + getPlanCounter() + ",recursionCounter=" + getRecursionCounter() + "]";
	}
	
}