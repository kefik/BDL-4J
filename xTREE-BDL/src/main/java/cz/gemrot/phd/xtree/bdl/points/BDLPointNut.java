package cz.gemrot.phd.xtree.bdl.points;

import java.util.HashSet;
import java.util.Set;

import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalCall;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalFinished;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalRecurse;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalReplace;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalResult;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint;

/**
 * Base implementation of BDL {@link IxTreePoint}.
 * 
 * @author Jakub Gemrot
 *
 * @param <AGENT>
 */
@AutoConfig
public class BDLPointNut<AGENT extends IBDLAgent> extends  BDLPointLayer<AGENT> {

	private Set<BDLLink> linksChanged = new HashSet<BDLLink>();
	
	private BDLLink<AGENT> lastLink = null; 

	private BDLPoint<AGENT> callerPoint = null;

	private BDLxSignalCall callerSignal = null;
	
	private BDLPointCore<AGENT> core;
	
	public BDLPointNut(BDLPointCore<AGENT> core) {
		this.core = core;
	}
	
	/**
	 * Copy-constructor.
	 * @param source
	 */
	public BDLPointNut(BDLPointNut<AGENT> source) {
		this.lastLink = source.lastLink;
		this.callerPoint = source.callerPoint;
		this.callerSignal = source.callerSignal;
		this.core = (BDLPointCore<AGENT>) source.core.clone();
	}
	
	@Override
	public void reset() {
		// REVERT LINKS
		for (BDLLink link : linksChanged) link.reset();
		linksChanged.clear();
		
		// RESET ITSELF
		callerPoint = null;
		callerSignal = null;
		
		// RESET THE CORE
		this.core.reset();
	}
	
	@Override
	public SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		// RESOLVE REPLACE ME
		if (signal instanceof BDLxSignalReplace) {
			// SANITY CHECK
			if (lastLink == null) {
				throw new RuntimeException("Invalid signal BDLxSignalReplace; nut has not been called yet.");
			}
			// UNWRAP THE SIGNAL
			BDLxSignalReplace<AGENT> replace = (BDLxSignalReplace<AGENT>)signal;
			// REPLACE TARGET OF THE LINK
			lastLink.setTarget(replace.getTarget());
			linksChanged.add(lastLink);
			// CALL THE NEW TARGET
			return new SelectResult(lastLink, replace.getSignal());
		}

		// HERE WE STORE SIGNAL RESULT
		SelectResult<AGENT> result = null;  
		
		// CALL SIGNAL?
		if (signal instanceof BDLxSignalCall) {

			if (callerPoint != null) {
				// loop!
				result = core.loopedCore(callerPoint, callerSignal, caller, signal, agent);
			} else {
				// standard call
				callerPoint  = caller;
				callerSignal = (BDLxSignalCall)signal;
				
				result = core.selectCore(callerPoint, callerSignal, caller, signal, agent);
			}
		} else 
		if (signal instanceof BDLxSignalResult) {
			// result signal is just passed
			result = core.selectCore(callerPoint, callerSignal, caller, signal, agent);
		} else {
			throw new RuntimeException("Nut is unable to handle signal: " + signal);
		}
		
		// ====================
		// DISSCUSS CORE RESULT
		// ====================
		
		BDLxSignal resultSignal = (BDLxSignal) result.getSignal();
		
		// SANITY CHECK
		if (callerSignal.isSignal(BDLxSignal.SIGNAL_TERMINATE)) {
			// WE CAN RETURN TERMINATED ONLY
			if (!resultSignal.isSignal(BDLxSignal.SIGNAL_TERMINATED)) {
				// BAD SIGNAL!
				throw new RuntimeException("Cannot reply with " + resultSignal + " when called with TERMINATE!");					
			}
		}
		
		// REACT TO RESULTS
		if (resultSignal instanceof BDLxSignalCall) {
			// CALL SIGNAL => mark the link followed
			lastLink = (BDLLink<AGENT>) result.getLink();					
		} else
		if (resultSignal instanceof BDLxSignalRecurse) {
			// RECURSE SIGNAL => mark the link followed
			lastLink = (BDLLink<AGENT>) result.getLink();
		} else 
		if (resultSignal instanceof BDLxSignalFinished || resultSignal instanceof BDLxSignalReplace) {
			// WE'VE FINISHED, CLEAN UP
			reset();
		} else
		if (resultSignal instanceof BDLxSignalResult) {
			// WE'RE RETURNING RESULT (currently it may be only RUNNING)
			// => clean up last caller point and signal
			callerPoint = null;
			callerSignal = null;
		} else {
			throw new RuntimeException("Unhanled result signal from core: " + resultSignal);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "Nut[core=" + core +"]";
	}
	
}
