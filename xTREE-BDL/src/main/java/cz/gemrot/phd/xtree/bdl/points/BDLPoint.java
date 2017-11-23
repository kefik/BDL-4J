package cz.gemrot.phd.xtree.bdl.points;

import cz.gemrot.phd.xtree.bdl.agent.IBDLAgent;
import cz.gemrot.phd.xtree.bdl.signals.BDLxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxSignal;
import cz.gemrot.phd.xtree.generic.iface.IxTreePoint;

public abstract class BDLPoint<AGENT extends IBDLAgent> implements IxTreePoint<AGENT> {

	@Override
	public final SelectResult<AGENT> select(IxTreePoint<AGENT> caller, IxSignal signal, AGENT agent) {
		return select((BDLPoint)caller, (BDLxSignal)signal, agent);
	}
	
	public abstract SelectResult<AGENT> select(BDLPoint<AGENT> caller, BDLxSignal signal, AGENT agent);

}
