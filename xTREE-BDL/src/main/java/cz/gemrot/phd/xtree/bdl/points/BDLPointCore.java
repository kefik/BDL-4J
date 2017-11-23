package cz.gemrot.phd.xtree.bdl.points;

import cz.gemrot.phd.utils.SocketArgs;
import cz.gemrot.phd.utils.sockets.IConfigurable;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalCall;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalResult;
import cz.gemrot.phd.xtree.generic.iface.IxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreeLink;

public abstract class BDLPointCore<AGENT extends IBDLAgent> extends BDLPointLayer<AGENT> implements IConfigurable<SocketArgs> {
		
	/**
	 * Configure the core using 'args';
	 * @param args
	 */
	@Override
	public abstract void configure(SocketArgs args);
	
	@Override
	public final SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		throw new RuntimeException("Call selectCore(...) instead!");
	}
	
	/**
	 * Standard signal, either call or result, it cannot be recurse or replace.
	 * @param caller
	 * @param currentCaller
	 * @param currentSignal
	 * @param agent
	 * @return
	 */
	public abstract SelectResult<AGENT> selectCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal currentSignal, AGENT agent);
	
	public final SelectResult<AGENT> looped(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		throw new RuntimeException("Call loopedCore(...) instead!");
	}
	
	/**
	 * New behavior iteration has begun.
	 * @param caller
	 * @param currentCaller
	 * @param signal
	 * @param agent
	 * @return
	 */
	public abstract SelectResult<AGENT> loopedCore(BDLPoint<AGENT> caller, BDLxSignal callerSignal, BDLPoint<AGENT> currentCaller, BDLxSignal currentSignal, AGENT agent);

	protected SelectResult call(IxTreeLink<AGENT> link, IxSignal signal) {
		return new SelectResult(link, signal);
	}
	
	protected SelectResult result(BDLPoint<AGENT> caller, IxSignal signal) {
		return new SelectResult(new BDLLink<AGENT>(caller), signal);
	}
	
	protected SelectResult<AGENT> execute(BDLLink<AGENT> link) {
		return new SelectResult(link, new BDLxSignalCall(BDLxSignal.SIGNAL_EXECUTE, null));
	}
	
	protected SelectResult<AGENT> switchOut(BDLLink<AGENT> link) {
		return new SelectResult(link, new BDLxSignalCall(BDLxSignal.SIGNAL_SWITCH, null));
	}
		
	protected SelectResult<AGENT> terminate(BDLLink<AGENT> link) {
		return new SelectResult(link, new BDLxSignalCall(BDLxSignal.SIGNAL_TERMINATE, null));
	}
	
	protected SelectResult running(BDLPoint<AGENT> caller) {
		return new SelectResult(new BDLLink<AGENT>(caller), new BDLxSignalResult(BDLxSignal.SIGNAL_RUNNING));
	}
	
	protected SelectResult success(BDLPoint<AGENT> caller) {
		return new SelectResult(new BDLLink<AGENT>(caller), new BDLxSignalResult(BDLxSignal.SIGNAL_SUCCESS));
	}
	
	protected SelectResult fail(BDLPoint<AGENT> caller) {
		return new SelectResult(new BDLLink<AGENT>(caller), new BDLxSignalResult(BDLxSignal.SIGNAL_FAIL));
	}
	
	protected SelectResult terminated(BDLPoint<AGENT> caller) {
		return new SelectResult(new BDLLink<AGENT>(caller), new BDLxSignalResult(BDLxSignal.SIGNAL_TERMINATED));
	}
	
	protected SelectResult replace(BDLPoint<AGENT> caller, BDLLink<AGENT> link, BDLxSignal signal) {
		return result(caller, BDLxSignal.REPLACE(link.getTarget(), signal));
	}
	
}
