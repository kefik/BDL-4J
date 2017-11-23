package cz.gemrot.phd.xtree.bdl.points;

import java.util.HashMap;

import cz.gemrot.phd.utils.sockets.AutoConfig;
import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.agent.ShellActivation;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalCall;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalFinished;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalRecurse;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignalResult;

@AutoConfig
public class BDLPointShell<AGENT extends IBDLAgent> extends BDLPointLayer<AGENT> {

	/**
	 * Prototype of the NUT we will be cloning into respective activations.
	 */
	private BDLPointNut<AGENT> protoNut;
	
	private HashMap<Integer, ShellActivation> activations = new HashMap<Integer, ShellActivation>();
 	
	public BDLPointShell(BDLPointNut<AGENT> protoNut) {
		this.protoNut = protoNut;
	}
	
	/**
	 * Copy-constructor.
	 * @param source
	 */
	public BDLPointShell(BDLPointShell<AGENT> source) {
		this.protoNut = source.protoNut;
	}
	
	@Override
	public SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent) {
		SelectResult<AGENT> result;
		
		// PERFORMING THE CALL
		
		if (signal instanceof BDLxSignalRecurse) {
			// WE HAVE BEEN RECURSED
			result = recurseSignal(caller, (BDLxSignalRecurse)signal, agent);
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
		
		// PATCH SIGNAL WITH CORRECT RECURSION NUMBER
		((BDLxSignal)result.getSignal()).setRecursionCounter(signal.getRecursionCounter());	

		// INTERPRETING RESULT OF THE CALL
		
		// POINT IS RETURNING RESULT TO CALLER?
		if (result.getSignal() instanceof BDLxSignalResult) {
			// POINT IS SENDING RESULT BACK TO THE CALLER
			// => update its recursion number
			
			// OBTAIN RESULT SIGNAL
			BDLxSignalResult resultSignal = (BDLxSignalResult)result.getSignal();
			
			// OBTAIN ACTIVATION FOR CALL SIGNAL RECURSION
			ShellActivation activation = getActivation(signal.getRecursionCounter());
			
			// CORRECT RESULT SIGNAL RECUSION NUMBER
			resultSignal.setRecursionCounter(activation.getResultRecursion());
		}
		
		// POINT HAS FINISHED?
		if (result.getSignal() instanceof BDLxSignalFinished) {
			// THE POINT HAS FINISHED
			// => cleanup
			int recursion = ((BDLxSignal)signal).getRecursionCounter();
			activations.remove(recursion);
		}
				
		return result;
	}
	
	private SelectResult<AGENT> callSignal(BDLPoint<AGENT> caller, BDLxSignalCall signal, AGENT agent) {
		int recursion = signal.getRecursionCounter();
		if (!hasActivation(recursion)) makeActivation(caller, recursion, recursion);
		return getActivation(recursion).getNut().select(caller, signal, agent);
	}

	private SelectResult<AGENT> resultSignal(BDLPoint<AGENT> caller, BDLxSignalResult signal, AGENT agent) {
		int recursion = signal.getRecursionCounter();
		if (!hasActivation(recursion)) {
			throw new RuntimeException("Shell[" + recursion + "] has not been called previously, cannot send result to it: " + signal);
		}
		return getActivation(recursion).getNut().select(caller, signal, agent);
	}

	private SelectResult<AGENT> recurseSignal(BDLPoint<AGENT> caller, BDLxSignalRecurse signal, AGENT agent) {
		// WE ARE PERFORMING RECURSION

		// GET OLD RECURSION NUMBER
		int recursion = signal.getRecursionCounter();
		
		// CREATE NEW RECURSION NUMBER
		int newRecursion = recursion + 1;
		
		// CREATE NEW ACTIVATION
		makeActivation(caller, newRecursion, recursion);
		
		// RETRIEVE SIGNAL THE RECURSE IS WRAPPING
		BDLxSignal recursionSignal = signal.getWrappedSignal();
		
		// UPDATE ITS RECURSION NUMBER
		recursionSignal.setRecursionCounter(newRecursion);
		
		// CALL THE POINT UNDER newRecursion
		return select(caller, recursionSignal, agent);
	}
	
	// ==========
	// SHELL DATA
	// ==========

	public ShellActivation makeActivation(BDLPoint caller, int recursionCounter, int resultRecusrionCounter) {
		if (activations.containsKey(recursionCounter)) return activations.get(recursionCounter);
		
		// IF WE DO NOT HAVE AN ACTIVATION FOR GIVEN RECURSION NUMBER
		// => we need to clone a new one as we have been called within new recursion

		// CREATE ACTIVATION
		// -- iteration number is restarted
		// -- we store resultRecursionCounter in order to correctly return results back
		ShellActivation activation = new ShellActivation(caller, recursionCounter, resultRecusrionCounter, protoNut.clone());
		
		activations.put(resultRecusrionCounter, activation);
		
		return activation;
	}
	
	private boolean hasActivation(int recursion) {
		return activations.containsKey(recursion);
	}

	public ShellActivation getActivation(int recursion) {
		if (!hasActivation(recursion)) throw new RuntimeException("Shell[" + recursion + "] has not been called previously, cannot return its activation.");
		return activations.get(recursion);
	}
	
	@Override
	public String toString() {
		return "Shell[#activations=" + activations.size() + ", protoNut=" + protoNut + "]";
	}

}
