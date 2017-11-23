package cz.gemrot.phd.xtree.bdl.signals;

import cz.gemrot.phd.utils.sockets.ConfigMap;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.points.BDLPoint;
import cz.gemrot.phd.xtree.generic.signals.xSignal;

/**
 * Implementation of all {@link xSignal}s used by BDL.
 *  
 * @author Jakub Gemrot
 */
public class BDLxSignal extends xSignal {
	
	// ============
	// SIGNAL NAMES
	// ============
	
	public static final String SIGNAL_EXECUTE = "EXECUTE";
	
	public static final String SIGNAL_SWITCH = "SWITCH";
	
	public static final String SIGNAL_TERMINATE = "TERMINATE";
	
	public static final String SIGNAL_RECURSE = "RECURSE";
	
	public static final String SIGNAL_SUCCESS = "SUCCESS";
	
	public static final String SIGNAL_FAIL = "FAIL";
	
	public static final String SIGNAL_RUNNING = "RUNNING";
	
	public static final String SIGNAL_TERMINATED = "TERMINATED";
	
	public static final String SIGNAL_REPLACE = "REPLACE";
	
	// ===============
	// FORWARD SIGNALS
	// ===============
	
	public static BDLxSignal EXECUTE = new BDLxSignalCall(SIGNAL_EXECUTE, null);
	
	public static BDLxSignal EXECUTE(ConfigMap userArgs) {
		return new BDLxSignalCall(SIGNAL_EXECUTE, userArgs);
	}
	
	public static BDLxSignal SWITCH = new BDLxSignalCall(SIGNAL_SWITCH, null);
	
	public static BDLxSignal SWITCH(ConfigMap userArgs) {
		return new BDLxSignalCall(SIGNAL_SWITCH, userArgs);
	}
		
	public static BDLxSignal TERMINATE = new BDLxSignalCall(SIGNAL_TERMINATE, null);
	
	public static BDLxSignal TERMINATE(ConfigMap userArgs) {
		return new BDLxSignalCall(SIGNAL_TERMINATE, userArgs);
	}
	
	// ==============
	// RESULT SIGNALS
	// ==============
	
	public static final BDLxSignal RUNNING = new BDLxSignalResult(SIGNAL_RUNNING);

	// ================
	// FINISHED SIGNALS
	// ================
	
	public static final BDLxSignal SUCCESS = new BDLxSignalFinished(SIGNAL_SUCCESS);

	public static final BDLxSignal FAIL = new BDLxSignalFinished(SIGNAL_FAIL);

	public static final BDLxSignal TERMINATED = new BDLxSignalFinished(SIGNAL_TERMINATED);

	// ===============
	// UTILITY SIGNALS
	// ===============
	
	public static final BDLxSignal DONE = new BDLxSignalResult(SIGNAL_RUNNING, true, null);
	
	public static BDLxSignal RECURSE(BDLxSignal backSignal, ConfigMap userArgs) {
		return new BDLxSignalRecurse(SIGNAL_RECURSE, backSignal, userArgs);
	}
	
	public static <AGENT extends IBDLAgent> BDLxSignal REPLACE(BDLPoint<AGENT> point, BDLxSignal signal) {
		return new BDLxSignalReplace<AGENT>(point, signal);
	}
	
	// =================================
	// Generic BDLxSignal Implementation 
	// =================================
	
	private int recursionCounter = 0;
	
	private int planCounter = 0;
	
	public BDLxSignal(String name) {
		super(name, false, null);
	}
	
	protected BDLxSignal(String name, ConfigMap userArgs) {
		super(name, false, userArgs);
	}
	
	protected BDLxSignal(String name, boolean done, ConfigMap userArgs) {
		super(name, done, userArgs);
	}	
	
	public int getPlanCounter() {
		return planCounter;
	}

	public void setPlanCounter(int planCounter) {
		this.planCounter = planCounter;
	}

	public int getRecursionCounter() {
		return recursionCounter;
	}

	public void setRecursionCounter(int recursion) {
		this.recursionCounter = recursion;
	}
	
	public BDLxSignal asSignal(String name) {
		BDLxSignal result = null;
		if (   SIGNAL_EXECUTE.equals(name) 
			|| SIGNAL_SWITCH.equals(name) 
			|| SIGNAL_TERMINATE.equals(name)) {
			result = new BDLxSignalCall(name, null); 
		} else
		if (   SIGNAL_SUCCESS.equals(name) 
			|| SIGNAL_FAIL.equals(name) 
			|| SIGNAL_TERMINATED.equals(name)) {
			result = new BDLxSignalFinished(name, null); 
		} else
		if (   SIGNAL_RUNNING.equals(name)) { 
			result = new BDLxSignalResult(name, null); 
		} else {
			throw new RuntimeException("Cannot mask as signal: " + name + ", extra arguments required...");
		}
			
		if (hasArgs()) result.setArgs(getArgs());
		result.recursionCounter = this.recursionCounter;
		result.planCounter = this.planCounter;
		return result;
	}
	
	public boolean isCall() {
		return this instanceof BDLxSignalCall;
	}
	
	public boolean isFinished() {
		return this instanceof BDLxSignalFinished;
	}
	
	public boolean isExecute() {
		return isSignal(SIGNAL_EXECUTE);
	}
	
	public boolean isSwitch() {
		return isSignal(SIGNAL_SWITCH);
	}
	
	public boolean isTerminate() {
		return isSignal(SIGNAL_TERMINATE);
	}
	
	public boolean isRecurse() {
		return this instanceof BDLxSignalRecurse;
	}
	
	public boolean isResult() {
		return this instanceof BDLxSignalResult;
	}
	
	public boolean isSuccess() {
		return isSignal(SIGNAL_SUCCESS);
	}
	
	public boolean isFail() {
		return isSignal(SIGNAL_FAIL);
	}
	
	public boolean isRunning() {
		return isSignal(SIGNAL_RUNNING);
	}
	
	public boolean isReplace() {
		return this instanceof BDLxSignalReplace;
	}
	
	@Override
	public String toString() {
		return "BDLxSignal[" + getName() + ",planCounter=" + getPlanCounter() + ",recursionCounter=" + recursionCounter + "]";
	}
	
}
