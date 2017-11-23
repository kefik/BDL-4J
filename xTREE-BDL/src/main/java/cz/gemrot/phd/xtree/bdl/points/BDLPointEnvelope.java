package cz.gemrot.phd.xtree.bdl.points;

import java.util.HashMap;

import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.EnvelopeActivation;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalCall;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalFinished;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalRecurse;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalResult;

@AutoConfig
public class BDLPointEnvelope<AGENT extends IBDLAgent> extends BDLPointLayer<AGENT> {

	/**
	 * Prototype of the SHELL we will be cloning into respective activations.
	 */
	private BDLPointShell<AGENT> protoShell;
	
	private HashMap<Integer, EnvelopeActivation> activations = new HashMap<Integer, EnvelopeActivation>();
 	
	public BDLPointEnvelope(BDLPointShell<AGENT> protoShell) {
		this.protoShell = protoShell;
	}
	
	@Override
	public SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		SelectResult<AGENT> result;
		
		// PERFORMING THE CALL
		if (signal instanceof BDLxSignalRecurse) {
			// WE HAVE BEEN RECURSED, BUT THAT'S NOT OUR BUSSINESS
			result = callSignal(caller, (BDLxSignalRecurse)signal, agent);
		} else
		if (signal instanceof BDLxSignalCall) {
			// THIS POINT HAS BEEN CALLED
			result = callSignal(caller, (BDLxSignalCall)signal, agent);
		} else
		if (signal instanceof BDLxSignalResult) {
			// CALLEE IS RETURNING A RESULT BACK TO US
			result =  resultSignal(caller, (BDLxSignalResult)signal, agent);
		} else {
			// INVALID SIGNAL!
			throw new RuntimeException("Unhandled signal: " + signal);
		}
		
		// PATCH SIGNAL WITH CORRECT PLAN NUMBER
		((BDLxSignal)result.getSignal()).setPlanCounter(signal.getPlanCounter());

		// POINT IS RETURNING RESULT TO CALLER?
		if (result.getSignal() instanceof BDLxSignalResult) {
			// POINT IS SENDING RESULT BACK TO THE CALLER
			// => update its plan counter number
			
			// OBTAIN RESULT SIGNAL
			BDLxSignalResult resultSignal = (BDLxSignalResult)result.getSignal();
			
			// OBTAIN ACTIVATION FOR CALL SIGNAL RECURSION
			EnvelopeActivation activation = getActivation(signal.getPlanCounter());
			
			// CORRECT RESULT SIGNAL RECUSION NUMBER
			resultSignal.setPlanCounter(activation.getResultPlanCounter());
		}
		
		// POINT HAS FINISHED?
		if (result.getSignal() instanceof BDLxSignalFinished) {
			// THE POINT HAS FINISHED
			// => cleanup
			int planCounter = ((BDLxSignal)signal).getPlanCounter();
			activations.remove(planCounter);
		}
				
		return result;
	}
	
	private SelectResult<AGENT> callSignal(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		int planCounter = signal.getPlanCounter();
		if (!hasActivation(planCounter)) makeActivation(caller, planCounter, planCounter);
		return getActivation(planCounter).getShell().select(caller, signal, agent);
	}

	private SelectResult<AGENT> resultSignal(BDLPoint<AGENT> caller, BDLxSignalResult signal, AGENT agent) {
		int planCounter = signal.getPlanCounter();
		if (!hasActivation(planCounter)) {
			throw new RuntimeException("Envelope[" + planCounter + "] has not been called previously, cannot send result to it: " + signal);
		}
		return getActivation(planCounter).getShell().select(caller, signal, agent);
	}

	// =============
	// ENVELOPE DATA
	// =============

	public EnvelopeActivation makeActivation(BDLPoint caller, int planCounter, int resultPlanCounter) {
		if (activations.containsKey(planCounter)) return activations.get(planCounter);
		
		// IF WE DO NOT HAVE AN ACTIVATION FOR GIVEN RECURSION NUMBER
		// => we need to clone a new one as we have been called within new recursion

		// CREATE ACTIVATION
		// -- iteration number is restarted
		// -- we store resultRecursionCounter in order to correctly return results back
		EnvelopeActivation activation = new EnvelopeActivation(caller, planCounter, resultPlanCounter, protoShell.clone());
		
		activations.put(resultPlanCounter, activation);
		
		return activation;
	}
	
	private boolean hasActivation(int planCounter) {
		return activations.containsKey(planCounter);
	}

	public EnvelopeActivation getActivation(int planCounter) {
		if (!hasActivation(planCounter)) throw new RuntimeException("Envelope[" + planCounter + "] has not been called previously, cannot return its activation.");
		return activations.get(planCounter);
	}
	
	@Override
	public String toString() {
		return "Envelope[#activations=" + activations.size() + ", protoShell=" + protoShell + "]";
	}

}
