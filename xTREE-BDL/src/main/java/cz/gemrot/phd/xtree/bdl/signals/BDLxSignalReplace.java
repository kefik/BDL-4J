package cz.gemrot.phd.xtree.bdl.signals;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;

/**
 * Utility signal - ReplaceMe signal.
 * 
 * @author Jakub Gemrot
 *
 * @param <AGENT>
 */
public class BDLxSignalReplace<AGENT extends IBDLAgent> extends BDLxSignalFinished {

	private BDLPoint<AGENT> target;
	
	private BDLxSignal signal;
	
	public BDLxSignalReplace(BDLPoint<AGENT> point, BDLxSignal signal) {
		super(SIGNAL_REPLACE);
		this.target = point;			
		this.signal = signal;
	}

	public BDLPoint<AGENT> getTarget() {
		return target;
	}

	public BDLxSignal getSignal() {
		return signal;
	}
	
	@Override
	public String toString() {
		return "BDLxSignalReplace[" + getName() + ",planCounter=" + getPlanCounter() + ",recursionCounter=" + getRecursionCounter() + ",target=" + target + ",signal=" + signal + "]";
	}
	
}
