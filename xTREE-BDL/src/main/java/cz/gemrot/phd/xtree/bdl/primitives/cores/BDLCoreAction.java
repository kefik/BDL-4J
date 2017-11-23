package cz.gemrot.phd.xtree.bdl.primitives.cores;

import cz.gemrot.phd.utils.SocketArgs;
import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointCore;
import cz.gemrot.phd.xtree.bdl.primitives.actions.IAction;
import cz.gemrot.phd.xtree.bdl.primitives.actions.IAction.ActionResult;
import cz.gemrot.phd.xtree.bdl.primitives.actions.SpelAction;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint.SelectResult;

@AutoConfig
public class BDLCoreAction<AGENT extends IBDLAgent> extends BDLPointCore<AGENT> {

	private IAction<AGENT> action;
	
	public BDLCoreAction(String spelCommand) {
		this.action = new SpelAction<AGENT>(spelCommand);
	}	
	
	public BDLCoreAction(IAction action) {
		this.action = action;
	}
	
	/**
	 * Copy-constructor
	 * @param source
	 */
	public BDLCoreAction(BDLCoreAction source) {
		this.action = source.action;
	}
	
	@Override
	public void configure(SocketArgs config) {
	}

	@Override
	public SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		ActionResult result = null;
		if (signal.isExecute()) {
			result = action.execute(agent);
		} else
		if (signal.isSwitch()) {
			result = action.switchOut(agent);
		} else
		if (signal.isTerminate()) {
			result = action.terminate(agent);
		}
		
		if (result == null) {
			return fail(caller);
		}
		
		switch (result) {
		case FAIL: return fail(caller);
		case RUNNING: return running(caller);
		case SUCCESS: return success(caller);
		case TERMINATED: return terminated(caller);
		default: throw new RuntimeException("Unhandled action result: " + result);
		}
	}

	@Override
	public SelectResult<AGENT> loopedCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		return selectCore(caller, callerSignal, currentCaller, signal, agent);
	}
	
	@Override
	public String toString() {
		return "Action[" + action + "]";
	}
	
}
