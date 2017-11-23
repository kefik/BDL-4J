package cz.gemrot.phd.xtree.bdl.points;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.generic.iface.IxTreeLink;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint;

/**
 * Base implementation of {@link IxTreeLink} that does not perform any body or reasoning actions.
 * 
 * @author Jakub Gemrot
 *
 * @param <AGENT>
 */
public class BDLLink<AGENT extends IBDLAgent> implements IxTreeLink<AGENT>, Cloneable {

	// =============
	// CONFIGURATION
	// =============
	
	private BDLPoint<AGENT> origTarget = null;

	// =======
	// RUNTIME
	// =======
	
	private BDLPoint<AGENT> target = null;
	
	public BDLLink() {
	}
	
	public BDLLink(BDLPoint<AGENT> target) {
		init(target);
	}
	
	protected void init(BDLPoint<AGENT> target) {
		this.origTarget = this.target = target;
	}
	
	public BDLLink<AGENT> clone() {
		BDLLink<AGENT> result = new BDLLink<AGENT>(origTarget);
		result.setTarget(target);
		return result;
	}
	
	@Override
	public BDLPoint<AGENT> getTarget() {
		return target;
	}

	@Override
	public void setTarget(IxTreePoint<AGENT> target) {
		this.target = (BDLPoint<AGENT>) target;
	}
	
	@Override
	public void performActions(AGENT agent) {
	}

	@Override
	public void reset() {
		target = origTarget;
	}
	
	@Override
	public String toString() {
		return "BDLLink[target=" + target + ",origTarget=" + origTarget + "]";
	}

}
