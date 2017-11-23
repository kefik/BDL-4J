package cz.gemrot.phd.xtree.bdl.primitives.cores;

import cz.gemrot.phd.utils.SocketArgs;
import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLLink;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointCore;
import cz.gemrot.phd.xtree.bdl.primitives.sensors.ICondition;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint.SelectResult;

@AutoConfig
public class BDLCoreDecide<AGENT extends IBDLAgent> extends BDLPointCore<AGENT> {

	private ICondition<AGENT> condition;	
	private BDLLink<AGENT> linkTrue;
	private BDLLink<AGENT> linkFalse;
	
	public BDLCoreDecide() {		
	}
	
	public BDLCoreDecide(ICondition condition, BDLPoint truePoint, BDLPoint falsePoint) {
		this.condition = condition;
		if (truePoint != null) {
			this.linkTrue = new BDLLink<AGENT>(truePoint);
		}
		if (falsePoint != null) {
			this.linkFalse = new BDLLink<AGENT>(falsePoint);
		}
	}
	
	/**
	 * Copy-constructor
	 * @param source
	 */
	public BDLCoreDecide(BDLCoreDecide source) {
		condition = source.condition;
		if (source.linkTrue != null) linkTrue = source.linkTrue.clone();
		if (source.linkFalse != null) linkFalse = source.linkFalse.clone();
		reset();
	}
	
	@Override
	public void configure(SocketArgs config) {
		condition = config.getCondition("cnd");
		linkTrue  = config.getLink("true");
		linkFalse = config.getLink("false");
	}
	
	@Override
	public void reset() {
		if (linkTrue != null) linkTrue.reset();
		if (linkFalse != null) linkFalse.reset();
	}

	@Override
	public SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		if (condition.evaluate(agent)) {
			if (linkTrue != null) {
				return replace(caller, linkTrue, signal);
			} else {
				return success(caller);
			}
		} else {
			if (linkFalse != null) {
				return replace(caller, linkFalse, signal);
			} else {
				return fail(caller);
			}
		}
	}

	@Override
	public SelectResult<AGENT> loopedCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		return selectCore(caller, callerSignal, currentCaller, signal, agent);
	}
	
	@Override
	public String toString() {
		return "Decide[cnd=" + condition + ",true=" + (linkTrue == null ? "undef" : "def") + ",false=" + (linkFalse == null ? "undef" : "def") + "]";
	}

}
