package cz.gemrot.phd.xtree.bdl.primitives.cores;

import cz.gemrot.phd.utils.SocketArgs;
import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLLink;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointCore;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint.SelectResult;

@AutoConfig
public class BDLCoreRoot<AGENT extends IBDLAgent> extends BDLPointCore<AGENT> {
	
	private BDLLink root;
	
	public BDLCoreRoot(BDLPoint target) {
		root = new BDLLink(target);
	}
	
	/**
	 * Copy-constructor
	 * @param source
	 */
	public BDLCoreRoot(BDLCoreRoot source) {
		root = source.root;
		reset();
	}
	
	@Override
	public void reset() {
		root.reset();
	}
	
	@Override
	public void configure(SocketArgs config) {
		BDLLink<AGENT> link = config.getLink("root");
		if (link != null) {
			root = link;
		}		
	}

	@Override
	public SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		if (signal.isResult()) {
			if (signal.getPlanCounter() == 0 && signal.getRecursionCounter() == 0) {
				return new SelectResult(null, BDLxSignal.DONE);
			} else {
				return new SelectResult(root, signal);
			}
		} else {
			return call(root, signal);			
		}
	}

	@Override
	public SelectResult<AGENT> loopedCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		return selectCore(caller, callerSignal, currentCaller, signal, agent);
	}
	
	@Override
	public String toString() {
		return "Root[root=" + root.getTarget() + "]";
	}

}
