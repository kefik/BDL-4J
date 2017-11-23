package cz.gemrot.phd.xtree.bdl.signals;

import cz.gemrot.phd.utils.sockets.ConfigMap;

/**
 * Utility signals - marks "RECURSE" signals.
 * 
 * @author Jakub Gemrot
 */
public class BDLxSignalRecurse extends BDLxSignal {
	
	/**
	 * Wrapped call signal...
	 */
	private BDLxSignal signal;
	
	public BDLxSignalRecurse(String name, BDLxSignal signal, ConfigMap userArgs) {
		super(name, userArgs);
		this.signal = signal; 
	}

	public BDLxSignal getWrappedSignal() {
		return signal;
	}
	
	@Override
	public String toString() {
		return "BDLxSignalRecurse[" + getName() + ",planCounter=" + getPlanCounter() + ",recursionCounter=" + getRecursionCounter() + ",signal=" + signal + "]";
	}
	
}