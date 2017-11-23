package cz.gemrot.phd.xtree.generic;

import cz.gemrot.phd.xtree.generic.agent.IAgent;
import cz.gemrot.phd.xtree.generic.iface.IxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint.SelectResult;

/**
 * xTREE implementation that follows Algorithm 6 from the thesis as close as practical.
 * 
 * @author Jakub Gemrot
 *
 * @param <AGENT>
 */
public class xTree<AGENT extends IAgent> {

	/**
	 * RUNTIME - original root of the xTREE.
	 */
	private IxTreePoint<AGENT> root;
	
	/**
	 * RUNTIME - signal that starts the search.
	 */
	private IxSignal rootSignal;

	/**
	 * RUNTIME - Caller of the {@link #currentPoint}.
	 */
	private IxTreePoint<AGENT> callerPoint;
	
	/**
	 * RUNTIME - current xTreePoint.
	 */
	private IxTreePoint<AGENT> currentPoint;
	
	/**
	 * RUNTIME - currently passed signal.
	 */
	private IxSignal currentSignal;
	
	public xTree(IxTreePoint<AGENT> root, IxSignal rootSignal) {
		this.root = root;
		this.rootSignal = this.currentSignal = rootSignal;
		this.currentPoint = root;
	}
	
	public IxSignal run(AGENT agent) {
		if (agent.getLog() != null) agent.getLog().info("xTree RUN!");
		
		if (this.currentSignal.isDone()) {
			this.currentPoint = this.root;
			this.currentSignal = this.rootSignal;
			this.callerPoint = null;
		}
		
		SelectResult<AGENT> option = null;
		
		// WHILE WE'RE NOT DONE... do the decision-making
		while (!currentSignal.isDone()) {
			
			if (agent.getLog() != null) agent.getLog().info("xTree: " + currentSignal + " > " + currentPoint);
			
			// CURRENT POINT GETS CALLED
			// -- if it wishes for, it executes Body and Reasoning actions directly via 'agent'
			option = currentPoint.select(callerPoint, currentSignal, agent);
						
			// WE'RE TRAVERSING SELECTED 'option'
			
			// REWRITE VALUES			
			callerPoint = currentPoint;
			currentSignal = option.getSignal();
			if (option.getLink() == null) {
				currentPoint = null;
			} else {
				currentPoint = option.getLink().getTarget();
			}
		}
		
		if (agent.getLog() != null) agent.getLog().info("xTree Finished: " + currentSignal);
		
		return currentSignal;
	}

}
