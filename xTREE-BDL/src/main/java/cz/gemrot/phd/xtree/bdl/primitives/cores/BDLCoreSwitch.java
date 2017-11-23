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
public class BDLCoreSwitch<AGENT extends IBDLAgent> extends BDLPointCore<AGENT> {

	private static enum CoreSwitchState {
		READY,
		EXECUTE_THEN,
		EXECUTE_ELSE,
		SWITCH_THEN_1,
		SWITCH_THEN_2,
		SWITCH_ELSE_1,
		SWITCH_ELSE_2,
		TERMINATE_THEN,
		TERMINATE_ELSE		
	};
	
	private CoreSwitchState state = CoreSwitchState.READY;
	
	private ICondition<AGENT> condition;	
	private BDLLink<AGENT> linkThen;
	private BDLLink<AGENT> linkElse;
	
	public BDLCoreSwitch() {		
	}
	
	public BDLCoreSwitch(ICondition condition, BDLPoint thenPoint, BDLPoint elsePoint) {
		this.condition = condition;
		this.linkThen = new BDLLink<AGENT>(thenPoint);
		this.linkElse = new BDLLink<AGENT>(elsePoint);
	}
	
	/**
	 * Copy-constructor
	 */
	public BDLCoreSwitch(BDLCoreSwitch source) {
		condition = source.condition;
		linkThen = source.linkThen.clone();
		linkElse = source.linkElse.clone();
	}
	
	
	@Override
	public void configure(SocketArgs config) {
		condition = config.getCondition("cnd");
		linkThen  = config.getLink("then");
		linkElse  = config.getLink("else");
	}

	@Override
	public void reset() {
		this.state = CoreSwitchState.READY;
		linkThen.reset();
		linkElse.reset();
	}
	
	@Override
	public SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal signal, AGENT agent) {
		switch (state) {
		case READY:
			if (condition.evaluate(agent)) {
				state = CoreSwitchState.EXECUTE_THEN;
				return execute(linkThen);
			} else {
				state = CoreSwitchState.EXECUTE_ELSE;
				return execute(linkElse);
			}
			
		// ===========
		// THEN BRANCH
		// ===========
			
		case EXECUTE_THEN:
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (condition.evaluate(agent)) {
					return execute(linkThen);
				} else {
					state = CoreSwitchState.SWITCH_THEN_2;
					return switchOut(linkThen);
				}				
			} else
			if (signal.isSwitch()) {
				state = CoreSwitchState.SWITCH_THEN_1;
				return switchOut(linkThen);
			} else
			if (signal.isTerminate()) {
				state = CoreSwitchState.TERMINATE_THEN;
				return terminate(linkThen);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		case SWITCH_THEN_1:
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (condition.evaluate(agent)) {
					state = CoreSwitchState.EXECUTE_THEN;
					return execute(linkThen);
				} else {
					state = CoreSwitchState.SWITCH_THEN_2;
					return switchOut(linkThen);
				}					
			} else
			if (signal.isSwitch()) {
				return switchOut(linkThen);
			} else
			if (signal.isTerminate()) {
				state = CoreSwitchState.TERMINATE_THEN;
				return terminate(linkThen);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		case TERMINATE_THEN:
			return terminated(caller);
			
		case SWITCH_THEN_2: 
			if (signal.isSuccess()) {
				state = CoreSwitchState.EXECUTE_ELSE;
				return execute(linkElse);
			} else
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (condition.evaluate(agent)) {
					state = CoreSwitchState.EXECUTE_THEN;
					return execute(linkThen);
				} else {
					return switchOut(linkThen);
				}					
			} else
			if (signal.isSwitch()) {
				state = CoreSwitchState.SWITCH_THEN_1;
				return switchOut(linkThen);
			} else
			if (signal.isTerminate()) {
				state = CoreSwitchState.TERMINATE_THEN;
				return terminate(linkThen);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		// ===========
		// THEN BRANCH
		// ===========
			
		case EXECUTE_ELSE:
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (!condition.evaluate(agent)) {
					return execute(linkElse);
				} else {
					state = CoreSwitchState.SWITCH_ELSE_2;
					return switchOut(linkElse);
				}				
			} else
			if (signal.isSwitch()) {
				state = CoreSwitchState.SWITCH_ELSE_1;
				return switchOut(linkElse);
			} else
			if (signal.isTerminate()) {
				state = CoreSwitchState.TERMINATE_ELSE;
				return terminate(linkElse);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		case SWITCH_ELSE_1:
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (!condition.evaluate(agent)) {
					state = CoreSwitchState.EXECUTE_ELSE;
					return execute(linkElse);
				} else {
					state = CoreSwitchState.SWITCH_ELSE_2;
					return switchOut(linkElse);
				}					
			} else
			if (signal.isSwitch()) {
				return switchOut(linkElse);
			} else
			if (signal.isTerminate()) {
				state = CoreSwitchState.TERMINATE_ELSE;
				return terminate(linkElse);
			} else {
				throw new RuntimeException("Unhandled signal (" + this.getClass().getSimpleName() + "." + state + "): " + signal);
			}
			
		case TERMINATE_ELSE:
			return terminated(caller);
			
		case SWITCH_ELSE_2: 
			if (signal.isSuccess()) {
				state = CoreSwitchState.EXECUTE_THEN;
				return execute(linkThen);
			} else
			if (signal.isResult()) {
				return result(caller, signal);
			} else
			if (signal.isExecute()) {
				if (!condition.evaluate(agent)) {
					state = CoreSwitchState.EXECUTE_ELSE;
					return execute(linkElse);
				} else {
					return switchOut(linkElse);
				}					
			} else
			if (signal.isSwitch()) {
				state = CoreSwitchState.SWITCH_ELSE_1;
				return switchOut(linkElse);
			} else
			if (signal.isTerminate()) {
				state = CoreSwitchState.TERMINATE_ELSE;
				return terminate(linkElse);
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
		return "Switch[cnd=" + condition + ",then=" + (linkThen == null ? "undef" : "def") + ",else=" + (linkElse == null ? "undef" : "def") + "]";
	}

}
