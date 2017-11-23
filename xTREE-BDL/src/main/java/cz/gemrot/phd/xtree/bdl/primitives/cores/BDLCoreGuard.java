package cz.gemrot.phd.xtree.bdl.primitives.cores;

import cz.gemrot.phd.utils.SocketArgs;
import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLLink;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.bdl.points.BDLPointCore;
import cz.gemrot.phd.xtree.bdl.primitives.sensors.ICondition;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;

@AutoConfig
public class BDLCoreGuard<AGENT extends IBDLAgent> extends BDLPointCore<AGENT> {

	private static enum CoreGuardState {
		READY,
		EXECUTE,
		SWITCH_OUT_1,
		SWITCH_OUT_2,
		TERMINATE
	};
	
	private CoreGuardState state = CoreGuardState.READY;
	
	private ICondition<AGENT> condition;	
	private BDLLink<AGENT> child;
	
	public BDLCoreGuard() {		
	}
	
	public BDLCoreGuard(ICondition condition, BDLPoint child) {
		this.condition = condition;
		this.child = new BDLLink<AGENT>(child);
	}
	
	/**
	 * Copy-constructor
	 */
	public BDLCoreGuard(BDLCoreGuard source) {
		condition = source.condition;
		child = source.child.clone();
	}
	
	
	@Override
	public void configure(SocketArgs config) {
		condition = config.getCondition("cnd");
		child  = config.getLink("child");
	}

	@Override
	public void reset() {
		this.state = CoreGuardState.READY;
		child.reset();
	}
	
	@Override
	public SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		switch (state) {
		case READY:
			if (condition.evaluate(agent)) {
				state = CoreGuardState.EXECUTE;
				return execute(child);
			} else {
				return fail(caller);
			}
			
		// ===========
		// THEN BRANCH
		// ===========
			
		case EXECUTE:
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (condition.evaluate(agent)) {
					return execute(child);
				} else {
					state = CoreGuardState.SWITCH_OUT_1;
					return switchOut(child);
				}				
			} else
			if (signal.isSwitch()) {
				state = CoreGuardState.SWITCH_OUT_2;
				return switchOut(child);
			} else
			if (signal.isTerminate()) {
				state = CoreGuardState.TERMINATE;
				return terminate(child);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		case SWITCH_OUT_1:
			if (signal.isFinished()) {
				return fail(caller);
			} else
			if (signal.isResult()) {
				return result(caller, signal);
			}
			if (signal.isExecute()) {
				if (condition.evaluate(agent)) {
					state = CoreGuardState.EXECUTE;
					return execute(child);
				} else {
					return switchOut(child);
				}					
			} else
			if (signal.isSwitch()) {
				state = CoreGuardState.SWITCH_OUT_2;
				return switchOut(child);
			} else
			if (signal.isTerminate()) {
				state = CoreGuardState.TERMINATE;
				return terminate(child);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		case TERMINATE:
			return terminated(caller);
			
		case SWITCH_OUT_2: 
			if (signal.isFinished()) {
				return result(currentCaller, signal);
			} else
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (condition.evaluate(agent)) {
					state = CoreGuardState.EXECUTE;
					return execute(child);
				} else {
					state = CoreGuardState.SWITCH_OUT_1;
					return switchOut(child);
				}					
			} else
			if (signal.isSwitch()) {
				return switchOut(child);
			} else
			if (signal.isTerminate()) {
				state = CoreGuardState.TERMINATE;
				return terminate(child);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
					
		}
		
		throw new RuntimeException("Unhandled state (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
	}

	@Override
	public SelectResult<AGENT> loopedCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		return selectCore(caller, callerSignal, currentCaller, signal, agent);
	}
	
	@Override
	public String toString() {
		return "Guard[cnd=" + condition + ",child=" + (child == null ? "undef" : "def") + "]";
	}

}
