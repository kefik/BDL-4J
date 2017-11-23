package cz.gemrot.phd.xtree.generic.iface;

import cz.gemrot.phd.xtree.generic.agent.IAgent;

public interface IxTreeLink<AGENT extends IAgent> {
	
	/**
	 * What xTreePoint is option referencing.
	 * @return
	 */
	public IxTreePoint<AGENT> getTarget();
	
	/**
	 * Way to manipulate the target of the option.
	 * @param target
	 */
	public void setTarget(IxTreePoint<AGENT> target);
		
	/**
	 * Callback for performing reasoning and body actions when traversing through this option.
	 * @param agent
	 */
	public void performActions(AGENT agent);
	
	/**
	 * Resets {@link #getTarget()}.
	 */
	public void reset();

}
